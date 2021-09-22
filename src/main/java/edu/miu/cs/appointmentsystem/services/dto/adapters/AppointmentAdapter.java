package edu.miu.cs.appointmentsystem.services.dto.adapters;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import edu.miu.cs.appointmentsystem.domain.Appointment;
import edu.miu.cs.appointmentsystem.services.dto.AppointmentDto;
import edu.miu.cs.appointmentsystem.services.dto.adapters.base.AdapterBase;
import edu.miu.cs.appointmentsystem.services.dto.adapters.base.CommonAdapter;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;
import edu.miu.cs.appointmentsystem.services.helpers.BeanQualifiersConstants;

@Component(BeanQualifiersConstants.APPOINTMENT_ADAPTER)
public class AppointmentAdapter extends CommonAdapter implements AdapterBase<Appointment, AppointmentDto> {

	@Override
	public Appointment toEntity(AppointmentDto dto) {
		Appointment appointment = new Appointment();
		appointment.setId(dto.getId());
		appointment.setDate(dto.getDate());
		appointment.setTime(dto.getTime());
		appointment.setLocation(dto.getLocation());
		return appointment;
	}

	@Override
	public AppointmentDto toDto(Appointment appointment) {
		AppointmentDto reservationDto = new AppointmentDto();
		updateDto(reservationDto, appointment);
		reservationDto.setDate(appointment.getDate());
		reservationDto.setTime(appointment.getTime());
		reservationDto.setLocation(appointment.getLocation());
		reservationDto.setDuration(appointment.getDuration());
		reservationDto.setAvailable(appointment.isAvailable());
		return reservationDto;
	}

	@Override
	public ListDtoBase<AppointmentDto> toDto(Page<Appointment> data) {
		ListDtoBase<AppointmentDto> appointmentDtoList = new ListDtoBase<>();
		appointmentDtoList.setTotal(data.getTotalElements());
		appointmentDtoList
				.setData(data.getContent().stream().map(this::toDto).collect(java.util.stream.Collectors.toList()));
		return appointmentDtoList;
	}

}
