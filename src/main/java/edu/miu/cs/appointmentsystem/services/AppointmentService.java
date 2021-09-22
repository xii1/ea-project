package edu.miu.cs.appointmentsystem.services;

import org.springframework.data.domain.Page;

import edu.miu.cs.appointmentsystem.common.enums.AppointmentAndReservationGetType;
import edu.miu.cs.appointmentsystem.exceptions.SystemBaseException;
import edu.miu.cs.appointmentsystem.integration.models.AppointmentInfo;
import edu.miu.cs.appointmentsystem.services.dto.AppointmentDto;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;

public interface AppointmentService {

	ListDtoBase<AppointmentDto> findAll(AppointmentAndReservationGetType getType, Long categoryId, Long providerId,
			Integer page, Integer size) throws SystemBaseException;

	AppointmentDto addAppointment(Long categoryId, Long providerId, AppointmentDto appointmentDtoDto)
			throws SystemBaseException;

	AppointmentDto updateAppointment(Long providerId, Long id, AppointmentDto appointmentDtoDto) throws SystemBaseException;

	void deleteAppointmentById(Long id) throws SystemBaseException;

	Page<AppointmentInfo> loadAppointmentsForReminder(Integer page, Integer size);

	void updateFirstSend(long id);

	void updateSecondSend(long id);

	AppointmentDto getById(AppointmentAndReservationGetType getType, Long pId, Long id) throws SystemBaseException;

}
