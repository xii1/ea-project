package edu.miu.cs.appointmentsystem.resources;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import edu.miu.cs.appointmentsystem.resources.helpers.Constants;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = Constants.RESERVATION)
@PropertySource(value = Constants.RESERVATION_FILE_PATH, factory = SystemMessageSourceFactory.class)
@Getter
@Setter
public class ReservationMessages {
    private String notFound;
    private String alreadyHasPendingReservation;
    private String alreadyReservedAppointment;
    private String sendIdAndObjectIdAreNotEqual;
    private String ReservationTypeProvidedNotSupported;
}
