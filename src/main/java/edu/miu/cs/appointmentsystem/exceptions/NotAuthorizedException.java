package edu.miu.cs.appointmentsystem.exceptions;

public class NotAuthorizedException extends SystemBaseException {
    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException() {

    }
}
