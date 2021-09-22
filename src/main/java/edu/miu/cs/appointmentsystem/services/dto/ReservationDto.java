package edu.miu.cs.appointmentsystem.services.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;

import edu.miu.cs.appointmentsystem.common.enums.ReservationStatus;
import edu.miu.cs.appointmentsystem.resources.ReservationMessages;
import edu.miu.cs.appointmentsystem.services.dto.base.DtoBase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReservationDto extends DtoBase {
    private static final String THE_DESCRIPTION_MUST_BE_NO_LONGER_THAN_500_CHARACTERS = "The note must be no longer than 500 characters.";
    private static final String RESERVATION_ID_IS_REQUIRED = "Reservation ID is required.";
    @Autowired
    private ReservationMessages reservationMessages;

    public ReservationDto() {
        status = ReservationStatus.PENDING;
    }

    @NotNull(message = RESERVATION_ID_IS_REQUIRED)
    private ReservationStatus status;
    @Length(max = 500, message = THE_DESCRIPTION_MUST_BE_NO_LONGER_THAN_500_CHARACTERS)
    private String note;
}
