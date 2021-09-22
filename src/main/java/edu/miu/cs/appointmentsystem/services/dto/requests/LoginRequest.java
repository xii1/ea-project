package edu.miu.cs.appointmentsystem.services.dto.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

/**
 * @author XIII
 */
@Getter
@Setter
public class LoginRequest {
    @Email(message = "An email format is required")
    private String email;

    private String password;
}
