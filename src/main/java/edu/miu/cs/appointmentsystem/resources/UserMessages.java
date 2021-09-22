package edu.miu.cs.appointmentsystem.resources;

import edu.miu.cs.appointmentsystem.resources.helpers.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = Constants.USER)
@PropertySource(value = Constants.USER_FILE_PATH, factory = SystemMessageSourceFactory.class)
@Getter
@Setter
public class UserMessages {
    private String notFound;
}
