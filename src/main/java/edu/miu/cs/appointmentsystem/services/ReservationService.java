package edu.miu.cs.appointmentsystem.services;

import edu.miu.cs.appointmentsystem.services.dto.ReservationDto;
import edu.miu.cs.appointmentsystem.common.enums.AppointmentAndReservationGetType;
import edu.miu.cs.appointmentsystem.exceptions.SystemBaseException;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;

public interface ReservationService {

        ReservationDto add(Long appointmentId, ReservationDto reservation) throws SystemBaseException;

        ReservationDto find(AppointmentAndReservationGetType getType, Long appointmentId, Long reservationId)
                        throws SystemBaseException;

        ReservationDto update(Long appointmentId, Long reservationId, ReservationDto reservation)
                        throws SystemBaseException;

        void delete(Long id) throws SystemBaseException;

        ListDtoBase<ReservationDto> findAll(AppointmentAndReservationGetType reservationGetType, Long appointmentId,
                        Long providerId, Integer page, Integer size) throws SystemBaseException;

}
