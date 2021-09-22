package edu.miu.cs.appointmentsystem.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import edu.miu.cs.appointmentsystem.domain.base.BaseEntity;
import edu.miu.cs.appointmentsystem.domain.helpers.Constants;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = Constants.Appointment.TABLE_NAME)
@Setter
@Getter
public class Appointment extends BaseEntity {
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime time;
    @Column(nullable = false)
    private LocalTime duration;
    private String location;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Constants.Appointment.COLUMN_NAME_PROVIDER_ID, nullable = false)
    private Provider provider;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Constants.Appointment.COLUMN_NAME_CATEGORY_ID, nullable = false)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = Constants.Reservation.APPOINTMENT_ID)
    @OrderBy(value = Constants.Appointment.DATE_ASC_TIME_ASC)
    private Collection<Reservation> reservations = new ArrayList<>();
    private boolean firstReminderSent;
    private boolean secondReminderSent;
    private boolean available;

    public Collection<Reservation> getReservations() {
        return Collections.unmodifiableCollection(reservations);
    }

    public boolean addReservation(Reservation reservation) {
        return reservations.add(reservation);
    }

    public boolean removeReservation(Reservation reservation) {
        return reservations.remove(reservation);
    }

}
