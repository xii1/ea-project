package edu.miu.cs.appointmentsystem.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.cs.appointmentsystem.common.enums.AppointmentAndReservationGetType;
import edu.miu.cs.appointmentsystem.common.enums.ReservationStatus;
import edu.miu.cs.appointmentsystem.domain.Appointment;
import edu.miu.cs.appointmentsystem.domain.Provider;
import edu.miu.cs.appointmentsystem.domain.Reservation;
import edu.miu.cs.appointmentsystem.domain.User;
import edu.miu.cs.appointmentsystem.exceptions.ClientDataIncorrectException;
import edu.miu.cs.appointmentsystem.exceptions.NotAuthorizedException;
import edu.miu.cs.appointmentsystem.exceptions.SystemBaseException;
import edu.miu.cs.appointmentsystem.exceptions.appointment.AppointmentAlreadyReservedException;
import edu.miu.cs.appointmentsystem.exceptions.appointment.AppointmentNotFoundException;
import edu.miu.cs.appointmentsystem.exceptions.provider.ProviderNotFoundException;
import edu.miu.cs.appointmentsystem.exceptions.reservation.ReservationNotFoundException;
import edu.miu.cs.appointmentsystem.integration.email.templates.AppointmentAcceptedTemplate;
import edu.miu.cs.appointmentsystem.integration.email.templates.AppointmentCanceledTemplate;
import edu.miu.cs.appointmentsystem.integration.email.templates.AppointmentDeclinedTemplate;
import edu.miu.cs.appointmentsystem.integration.email.templates.AppointmentReservationTemplate;
import edu.miu.cs.appointmentsystem.integration.email.templates.AppointmentTemplate;
import edu.miu.cs.appointmentsystem.integration.email.templates.enums.EmailTemplateFor;
import edu.miu.cs.appointmentsystem.integration.helpers.Constants;
import edu.miu.cs.appointmentsystem.integration.models.AppointmentInfo;
import edu.miu.cs.appointmentsystem.integration.models.Email;
import edu.miu.cs.appointmentsystem.repositories.AppointmentRepository;
import edu.miu.cs.appointmentsystem.repositories.ProviderRepository;
import edu.miu.cs.appointmentsystem.repositories.ReservationRepository;
import edu.miu.cs.appointmentsystem.resources.ReservationMessages;
import edu.miu.cs.appointmentsystem.services.dto.ReservationDto;
import edu.miu.cs.appointmentsystem.services.dto.adapters.base.AdapterBase;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;
import edu.miu.cs.appointmentsystem.services.helpers.BeanQualifiersConstants;
import edu.miu.cs.appointmentsystem.integration.email.EmailSender;
import org.springframework.jms.core.JmsTemplate;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ReservationMessages reservationMessages;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private EmailSender emailSender;
    @Autowired
    @Qualifier(BeanQualifiersConstants.RESERVATION_ADAPTER)
    private AdapterBase<Reservation, ReservationDto> AdapterBase;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public ReservationDto add(Long appointmentId, ReservationDto reservationDto) throws SystemBaseException {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        Appointment appointment = optionalAppointment
                .orElseThrow(() -> new AppointmentNotFoundException(reservationMessages.getNotFound()));
        User user = userService.getCurrentUser();

        if (reservationRepository.countByUserIdAndStatus(appointmentId, user.getId(), ReservationStatus.PENDING) == 0) {
            Reservation reservation = AdapterBase.toEntity(reservationDto);
            reservation.setCreatedBy(user);
            reservation.setCreatedDate(LocalDateTime.now());
            appointment.addReservation(reservation);
            reservationRepository.flush();
            ReservationDto finalResult = AdapterBase.toDto(reservation);
            // Send email to Client
            SendEmail(optionalAppointment.get(), reservation,
                    new AppointmentReservationTemplate(EmailTemplateFor.Client));
            // send email to Provider
            SendEmail(optionalAppointment.get(), reservation,
                    new AppointmentReservationTemplate(EmailTemplateFor.Provider));
            // schedule reminder emails
            jmsTemplate.convertAndSend(Constants.SCHEDULE_NEW_RESERVATION_REMINDERS,
                    new AppointmentInfo(appointmentId, appointment.getProvider().getName(), appointment.getDate(),
                            appointment.getTime(), appointment.getDuration(), appointment.getLocation(),
                            user.getFullName(), user.getEmail(), reservation.getStatus(), reservation.getNote()));
            return finalResult;
        } else {
            throw new ClientDataIncorrectException(reservationMessages.getAlreadyHasPendingReservation());
        }

    }

    @Override
    public ReservationDto update(Long appointmentId, Long reservationId, ReservationDto reservation)
            throws SystemBaseException {
        Optional<Reservation> dbReservationO = reservationRepository.findByIdForUpdate(reservation.getId());

        Reservation dbReservation = dbReservationO
                .orElseThrow(() -> new ReservationNotFoundException(reservationMessages.getNotFound()));
        if (dbReservation.getId().equals(reservation.getId())) {
            User user = userService.getCurrentUser();
            Optional<Appointment> optionalAppointment = appointmentRepository.findByIdForUpdate(appointmentId);
            Appointment appointment = optionalAppointment
                    .orElseThrow(() -> new AppointmentNotFoundException(reservationMessages.getNotFound()));
            if (appointment.getProvider().getUsers().stream().filter(x -> x.getId().equals(user.getId())).count() > 0) {
                if (reservation.getStatus() == ReservationStatus.ACCEPTED) {
                    if (appointment.isAvailable()) {
                        appointment.setAvailable(false);
                    } else {
                        throw new AppointmentAlreadyReservedException(
                                reservationMessages.getAlreadyReservedAppointment());
                    }
                } else {
                    appointment.setAvailable(true);
                    appointment.setFirstReminderSent(false);
                    appointment.setSecondReminderSent(false);
                }
                dbReservation.setStatus(reservation.getStatus());
                dbReservation.setNote(reservation.getNote());
                dbReservation.setStatus(reservation.getStatus());
                reservationRepository.flush();
                ReservationDto finalResult = AdapterBase.toDto(dbReservation);
                sendNotifications(appointment, dbReservation);
                return finalResult;
            } else {
                throw new NotAuthorizedException();
            }
        } else {
            throw new ClientDataIncorrectException(reservationMessages.getSendIdAndObjectIdAreNotEqual());
        }
    }

    private void sendNotifications(Appointment appointment, Reservation dbReservation) {
        AppointmentTemplate template = null;

        switch (dbReservation.getStatus()) {
            case ACCEPTED:
                template = new AppointmentAcceptedTemplate();
                break;
            case CANCELED:
                template = new AppointmentCanceledTemplate();
                break;
            case DECLINED:
                template = new AppointmentDeclinedTemplate();
                break;
            default:
                break;
        }
        SendEmail(appointment, dbReservation, template);

    }

    private void SendEmail(Appointment appointment, Reservation dbReservation, AppointmentTemplate template) {
        if (template != null) {
            AppointmentInfo appointmentInfo = new AppointmentInfo(appointment.getId(),
                    appointment.getProvider().getName(), appointment.getDate(), appointment.getTime(),
                    appointment.getDuration(), appointment.getLocation(), dbReservation.getCreatedBy().getFullName(),
                    dbReservation.getCreatedBy().getEmail(), dbReservation.getStatus(), dbReservation.getNote());
            appointmentInfo.setProviderEmail(appointment.getProvider().getUsers().stream().map(u -> u.getEmail())
                    .reduce("", (c, n) -> c + ";" + n));
            Email email = template.getTemplate(appointmentInfo);
            emailSender.SendEmail(email);
        }
    }

    @Override
    public void delete(Long id) throws SystemBaseException {
        Optional<Reservation> dbReservationO = reservationRepository.findById(id);
        Reservation dbReservation = dbReservationO
                .orElseThrow(() -> new ReservationNotFoundException(reservationMessages.getNotFound()));
        if (dbReservation.getCreatedBy().getId().equals(userService.getCurrentUser().getId())) {
            reservationRepository.delete(dbReservation);
        } else {
            throw new NotAuthorizedException();
        }

    }

    private ListDtoBase<ReservationDto> findAllForProvider(Long appointmentId, Long providerId, Integer page,
            Integer size) throws SystemBaseException {
        Optional<Provider> dbProviderO = providerRepository.findById(providerId);
        Provider dbProvider = dbProviderO
                .orElseThrow(() -> new ProviderNotFoundException(reservationMessages.getNotFound()));
        User user = userService.getCurrentUser();
        if (dbProvider.getUsers().stream().filter(x -> x.getId().equals(user.getId())).count() > 0) {
            Pageable request = PageRequest.of(page, size);
            return AdapterBase
                    .toDto(reservationRepository.findByAppointmentIdForProvider(appointmentId, providerId, request));
        } else {
            throw new NotAuthorizedException();
        }
    }

    private ListDtoBase<ReservationDto> findAllForClient(Long appointmentId, Integer page, Integer size) {
        Pageable request = PageRequest.of(page, size);
        User user = userService.getCurrentUser();
        return AdapterBase
                .toDto(reservationRepository.findByAppointmentIdForClient(appointmentId, user.getId(), request));
    }

    @Override
    public ReservationDto find(AppointmentAndReservationGetType getType, Long appointmentId, Long reservationId)
            throws SystemBaseException {
        Optional<Reservation> dbReservationO = reservationRepository.findById(reservationId);
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        Appointment appointment = optionalAppointment
                .orElseThrow(() -> new AppointmentNotFoundException(reservationMessages.getNotFound()));
        User user = userService.getCurrentUser();
        switch (getType) {
            case Provider:
                if (appointment.getProvider().getUsers().stream().filter(x -> x.getId().equals(user.getId()))
                        .count() > 0) {
                    Reservation dbReservation = dbReservationO
                            .orElseThrow(() -> new ReservationNotFoundException(reservationMessages.getNotFound()));
                    return AdapterBase.toDto(dbReservation);
                } else {
                    throw new NotAuthorizedException();
                }
            case Client:
                Reservation dbReservation = dbReservationO
                        .orElseThrow(() -> new ReservationNotFoundException(reservationMessages.getNotFound()));

                if (dbReservation.getCreatedBy().getId().equals(user.getId())) {
                    return AdapterBase.toDto(dbReservation);
                } else {
                    throw new NotAuthorizedException();
                }
            default:
                throw new ClientDataIncorrectException(reservationMessages.getReservationTypeProvidedNotSupported());
        }

    }

    @Override
    public ListDtoBase<ReservationDto> findAll(AppointmentAndReservationGetType reservationGetType, Long appointmentId,
            Long providerId, Integer page, Integer size) throws SystemBaseException {
        switch (reservationGetType) {
            case Provider:
                return findAllForProvider(appointmentId, providerId, page, size);
            case Client:
                return findAllForClient(appointmentId, page, size);
            default:
                throw new ClientDataIncorrectException(reservationMessages.getReservationTypeProvidedNotSupported());
        }
    }
}
