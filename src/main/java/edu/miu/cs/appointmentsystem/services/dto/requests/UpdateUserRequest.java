package edu.miu.cs.appointmentsystem.services.dto.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author XIII
 */
@Getter
@Setter
public class UpdateUserRequest {
    private List<String> roles;
}
