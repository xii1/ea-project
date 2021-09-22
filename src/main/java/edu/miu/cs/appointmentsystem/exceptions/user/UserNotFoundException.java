package edu.miu.cs.appointmentsystem.exceptions.user;

import edu.miu.cs.appointmentsystem.exceptions.SystemNotFoundException;

public class UserNotFoundException extends SystemNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {

    }
}
