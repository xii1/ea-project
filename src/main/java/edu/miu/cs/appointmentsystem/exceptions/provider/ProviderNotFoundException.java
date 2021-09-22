package edu.miu.cs.appointmentsystem.exceptions.provider;

import edu.miu.cs.appointmentsystem.exceptions.SystemNotFoundException;

public class ProviderNotFoundException extends SystemNotFoundException {
    public ProviderNotFoundException(String message) {
        super(message);
    }

    public ProviderNotFoundException() {

    }
}
