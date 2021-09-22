package edu.miu.cs.appointmentsystem.exceptions.provider;

import edu.miu.cs.appointmentsystem.exceptions.SystemAlreadyExistException;

public class UserInProviderAlreadyExistException extends SystemAlreadyExistException {

    public UserInProviderAlreadyExistException(String message) {
        super(message);
    }

    public UserInProviderAlreadyExistException() {

    }
}