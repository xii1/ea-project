package edu.miu.cs.appointmentsystem.exceptions.appointment;

import edu.miu.cs.appointmentsystem.exceptions.SystemNotFoundException;

public class AppointmentNotFoundException extends SystemNotFoundException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }

    public AppointmentNotFoundException() {

    }
}
