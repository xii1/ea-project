package edu.miu.cs.appointmentsystem.exceptions.provider;

import edu.miu.cs.appointmentsystem.exceptions.SystemNotFoundException;

public class UserInProviderNotFoundException extends SystemNotFoundException {
    public UserInProviderNotFoundException(String message) {
        super(message);
    }

    public UserInProviderNotFoundException() {

    }
}
