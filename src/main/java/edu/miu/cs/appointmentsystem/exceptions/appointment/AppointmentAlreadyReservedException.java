package edu.miu.cs.appointmentsystem.exceptions.appointment;

import edu.miu.cs.appointmentsystem.exceptions.SystemNotFoundException;

public class AppointmentAlreadyReservedException extends SystemNotFoundException {

    public AppointmentAlreadyReservedException(String message) {
        super(message);
    }

    public AppointmentAlreadyReservedException() {

    }
}