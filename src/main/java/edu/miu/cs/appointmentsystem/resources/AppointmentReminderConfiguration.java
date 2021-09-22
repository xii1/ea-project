package edu.miu.cs.appointmentsystem.resources;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import edu.miu.cs.appointmentsystem.resources.helpers.Constants;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = Constants.RESERVATION_REMINDER_CONFIG)
@PropertySource(value = Constants.RESERVATION_FILE_PATH, factory = SystemMessageSourceFactory.class)
@Getter
@Setter
public class AppointmentReminderConfiguration {
    private int firstReminder;
    private int secondReminder;
    private int batchSize;
}
