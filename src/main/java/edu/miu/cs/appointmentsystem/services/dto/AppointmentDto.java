package edu.miu.cs.appointmentsystem.services.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import edu.miu.cs.appointmentsystem.services.dto.base.DtoBase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AppointmentDto extends DtoBase {
	private static final String DATE_SHOULD_BE_IN_THE_FUTURE = "Date should be in the future";
	private static final String DEFAULT_DATE_REQUIRED = "The default date is required.";
	private static final String DEFAULT_TIME_REQUIRED = "The default time is required.";
	private static final String APPOINTMENT_LOCATION_REQUIRED = "The location is required.";
	private static final String APPOINTMENT_LOCATION_MIN_LENGTH = "The location must be more than 2 characters.";
	private static final String APPOINTMENT_LOCATION_MAX_LENGTH = "The note must be no longer than 500 characters.";
	@NotNull(message = DEFAULT_DATE_REQUIRED)
	@Future(message = DATE_SHOULD_BE_IN_THE_FUTURE)
	private LocalDate date;
	@NotNull(message = DEFAULT_TIME_REQUIRED)
	private LocalTime time;
	private LocalTime duration;
	@NotEmpty(message = APPOINTMENT_LOCATION_REQUIRED)
	@Length(min = 2, message = APPOINTMENT_LOCATION_MIN_LENGTH)
	@Length(max = 500, message = APPOINTMENT_LOCATION_MAX_LENGTH)
	private String location;
	@NotNull
	private boolean available;

}
