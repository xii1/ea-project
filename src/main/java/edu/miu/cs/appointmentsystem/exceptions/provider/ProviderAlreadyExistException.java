package edu.miu.cs.appointmentsystem.exceptions.provider;

import edu.miu.cs.appointmentsystem.exceptions.SystemAlreadyExistException;

public class ProviderAlreadyExistException extends SystemAlreadyExistException {

    public ProviderAlreadyExistException(String message) {
        super(message);
    }

    public ProviderAlreadyExistException() {

    }
}