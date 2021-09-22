package edu.miu.cs.appointmentsystem.integration.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import edu.miu.cs.appointmentsystem.common.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentInfo implements Serializable {

    public AppointmentInfo(long id, String providerName, LocalDate appointmentDate, LocalTime appointmentTime,
            LocalTime duration, String location, String clientName, String clientEmail,
            ReservationStatus reservationStatus, String notes) {
        this.id = id;
        this.providerName = providerName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.duration = duration;
        this.location = location;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.reservationStatus = reservationStatus;
        this.notes = notes;
    }

    private long id;
    private String providerName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalTime duration;
    private String location;
    private String clientName;
    private String clientEmail;
    private ReservationStatus reservationStatus;
    private String notes;
    private Long clientId;
    private Long providerId;
    private boolean isFirstReminder;
    private boolean isSecondReminder;
    private String providerEmail;
}
