package edu.miu.cs.appointmentsystem.exceptions.reservation;

import edu.miu.cs.appointmentsystem.exceptions.SystemNotFoundException;

public class ReservationNotFoundException extends SystemNotFoundException {
    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException() {

    }
}
