package edu.miu.cs.appointmentsystem.services;

import edu.miu.cs.appointmentsystem.domain.Category;
import edu.miu.cs.appointmentsystem.repositories.CategoryRepository;
import edu.miu.cs.appointmentsystem.repositories.ProviderRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.cs.appointmentsystem.common.enums.AppointmentAndReservationGetType;
import edu.miu.cs.appointmentsystem.domain.Appointment;
import edu.miu.cs.appointmentsystem.domain.Provider;
import edu.miu.cs.appointmentsystem.domain.User;
import edu.miu.cs.appointmentsystem.exceptions.ClientDataIncorrectException;
import edu.miu.cs.appointmentsystem.exceptions.NotAuthorizedException;
import edu.miu.cs.appointmentsystem.exceptions.SystemBaseException;
import edu.miu.cs.appointmentsystem.exceptions.appointment.AppointmentNotFoundException;
import edu.miu.cs.appointmentsystem.exceptions.provider.ProviderNotFoundException;
import edu.miu.cs.appointmentsystem.integration.models.AppointmentInfo;
import edu.miu.cs.appointmentsystem.repositories.AppointmentRepository;
import edu.miu.cs.appointmentsystem.services.dto.AppointmentDto;
import edu.miu.cs.appointmentsystem.services.dto.adapters.base.AdapterBase;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;
import edu.miu.cs.appointmentsystem.services.helpers.BeanQualifiersConstants;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AppointmentServiceImpl implements AppointmentService {
	@Autowired
	private AppointmentRepository repositoryAppointment;
	@Autowired
	private ProviderRepository providerRepository;

	@Autowired
	private CategoryRepository repositoryCategory;

	@Autowired
	private UserService userService;

	@Autowired
	@Qualifier(BeanQualifiersConstants.APPOINTMENT_ADAPTER)
	private AdapterBase<Appointment, AppointmentDto> AdapterBase;

	@Override
	public AppointmentDto getById(AppointmentAndReservationGetType getType, Long pId, Long id)
			throws SystemBaseException {
		User user = userService.getCurrentUser();
		Optional<Provider> optionalProvider = providerRepository.findById(pId);
		Provider provider = optionalProvider.orElseThrow(() -> new ProviderNotFoundException("provider id not found"));
		Optional<Appointment> optionalAppointment = repositoryAppointment.findById(id);
		Appointment appointment = optionalAppointment
				.orElseThrow(() -> new AppointmentNotFoundException("appointment id not found"));
		switch (getType) {
			case Provider:

				if (provider.getUsers().stream().filter(x -> x.getId().equals(user.getId())).count() > 0
						&& appointment.getProvider().getId() == pId) {
					return AdapterBase.toDto(appointment);
				} else {
					throw new NotAuthorizedException(
							"This appointment is not yours or you dont have permission to view it");
				}
			case Client:
				appointment = repositoryAppointment
						.findByProviderIdAndAppointmentIdAndUserId(id, appointment.getId(), user.getId())
						.orElseThrow(() -> new NotAuthorizedException("You cant access this appointment"));
				return AdapterBase.toDto(appointment);

			default:
				throw new ClientDataIncorrectException("UnKnown Appointment And Reservation Get Type");
		}
	}

	@Override
	public ListDtoBase<AppointmentDto> findAll(AppointmentAndReservationGetType getType, Long categoryId,
			Long providerId, Integer page, Integer size) throws SystemBaseException {
		Pageable request = PageRequest.of(page, size, Direction.ASC, "id");
		Optional<Provider> optionalProvider = providerRepository.findById(providerId);
		optionalProvider.orElseThrow(() -> new ProviderNotFoundException("provider id not found"));
		User user = userService.getCurrentUser();
		switch (getType) {
			case Provider:
				return AdapterBase.toDto(repositoryAppointment.findAllByCategoryAndProviderForProvider(categoryId,
						providerId, user.getId(), request));
			case Client:
				return AdapterBase.toDto(repositoryAppointment.findAllByCategoryAndProviderForClient(categoryId,
						providerId, user.getId(), request));
			default:
				throw new ClientDataIncorrectException("UnKnown Appointment And Reservation Get Type");
		}

	}

	@Override
	public AppointmentDto addAppointment(Long categoryId, Long providerId, AppointmentDto appointmentDto)
			throws SystemBaseException {
		User user = userService.getCurrentUser();
		Optional<Provider> optionalProvider = providerRepository.findById(providerId);
		Provider provider = optionalProvider.orElseThrow(() -> new ProviderNotFoundException("provider id not found"));
		if (provider.getUsers().stream().filter(x -> x.getId().equals(user.getId())).count() > 0) {

			Category category = repositoryCategory.getById(categoryId);
			Appointment appointment = new Appointment();
			appointment.setDate(appointmentDto.getDate());
			appointment.setTime(appointmentDto.getTime());
			appointment.setDuration(appointmentDto.getDuration());
			if (appointment.getDuration() == null) {
				appointment.setDuration(category.getDefaultDuration());
			}
			appointment.setLocation(appointmentDto.getLocation());
			appointment.setAvailable(appointmentDto.isAvailable());
			appointment.setCategory(category);
			appointment.setProvider(provider);
			return AdapterBase.toDto(repositoryAppointment.save(appointment));
		} else {
			throw new NotAuthorizedException(
					"This access to this provider, you need to get permission from the system admin");
		}
	}

	@Override
	public AppointmentDto updateAppointment(Long providerId, Long id, AppointmentDto appointmentDto)
			throws SystemBaseException {
		User user = userService.getCurrentUser();
		Optional<Appointment> optionalAppointment = repositoryAppointment.findById(id);
		Appointment appointment = optionalAppointment
				.orElseThrow(() -> new AppointmentNotFoundException("appointment id not found"));

		Optional<Provider> optionalProvider = providerRepository.findById(providerId);
		Provider provider = optionalProvider.orElseThrow(() -> new ProviderNotFoundException("provider id not found"));
		if (provider.getUsers().stream().filter(x -> x.getId().equals(user.getId())).count() > 0 && appointment
				.getProvider().getUsers().stream().filter(x -> x.getId().equals(user.getId())).count() > 0) {
			System.out.println(appointmentDto.toString());
			appointment.setDate(appointmentDto.getDate());
			appointment.setTime(appointmentDto.getTime());
			appointment.setDuration(appointmentDto.getDuration());
			appointment.setLocation(appointmentDto.getLocation());
			appointment.setAvailable(appointmentDto.isAvailable());
			return AdapterBase.toDto(repositoryAppointment.save(appointment));
		} else {
			throw new NotAuthorizedException("You can't access this appointment, its for another provider");
		}
	}

	@Override
	public void deleteAppointmentById(Long id) throws SystemBaseException {
		User user = userService.getCurrentUser();
		Optional<Appointment> optionalAppointment = repositoryAppointment.findById(id);
		Appointment appointment = optionalAppointment
				.orElseThrow(() -> new AppointmentNotFoundException("appointment id not found"));
		if (appointment.getProvider().getUsers().stream().filter(x -> x.getId().equals(user.getId())).count() > 0) {
			repositoryAppointment.deleteById(id);
		} else {
			throw new NotAuthorizedException("You can't access this appointment, its for another provider");
		}
	}

	@Override
	public Page<AppointmentInfo> loadAppointmentsForReminder(Integer page, Integer size) {
		Pageable request = PageRequest.of(page, size, Direction.ASC, "id");
		return repositoryAppointment.loadAppointmentInfosForReminders(request);
	}

	@Override
	public void updateFirstSend(long id) {
		repositoryAppointment.updateFirstReminder(id);

	}

	@Override
	public void updateSecondSend(long id) {
		repositoryAppointment.updateSecondReminder(id);

	}

}
