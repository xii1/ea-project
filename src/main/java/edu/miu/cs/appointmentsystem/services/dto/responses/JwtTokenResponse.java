package edu.miu.cs.appointmentsystem.services.dto.responses;

import edu.miu.cs.appointmentsystem.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author XIII
 */
@Getter
@Setter
@AllArgsConstructor
public class JwtTokenResponse {
    private String token;
    private User user;
}
