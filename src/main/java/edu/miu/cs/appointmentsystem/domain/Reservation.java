package edu.miu.cs.appointmentsystem.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import edu.miu.cs.appointmentsystem.common.enums.ReservationStatus;
import edu.miu.cs.appointmentsystem.domain.base.BaseEntity;
import edu.miu.cs.appointmentsystem.domain.helpers.Constants;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = Constants.Reservation.TABLE_NAME)
@Getter
@Setter
public class Reservation extends BaseEntity {
    public Reservation() {
        status = ReservationStatus.PENDING;
    }

    @Column(nullable = false)
    private ReservationStatus status;
    @Column(length = 500)
    private String note;
}
