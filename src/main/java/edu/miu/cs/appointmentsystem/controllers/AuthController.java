package edu.miu.cs.appointmentsystem.controllers;

import edu.miu.cs.appointmentsystem.controllers.base.ControllerBase;
import edu.miu.cs.appointmentsystem.controllers.helper.RestConstants;
import edu.miu.cs.appointmentsystem.domain.User;
import edu.miu.cs.appointmentsystem.domain.enums.Gender;
import edu.miu.cs.appointmentsystem.domain.enums.Role;
import edu.miu.cs.appointmentsystem.services.UserService;
import edu.miu.cs.appointmentsystem.services.dto.requests.LoginRequest;
import edu.miu.cs.appointmentsystem.services.dto.requests.RegisterRequest;
import edu.miu.cs.appointmentsystem.services.dto.responses.JwtTokenResponse;
import edu.miu.cs.appointmentsystem.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author XIII
 */
@RestController
@RequestMapping(RestConstants.Authentication.PREFIX)
public class AuthController extends ControllerBase {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @RequestMapping(value = RestConstants.Authentication.CHECK_EXISTS_EMAIL, method = RequestMethod.HEAD)
    public ResponseEntity<?> exists(@PathVariable String email) {
        final boolean isExists = userService.checkExists(email);
        return new ResponseEntity<>(isExists ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping(RestConstants.Authentication.REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userService.checkExists(registerRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(CreateResponse(false, null, Arrays.asList("The email is registered")));
        }

        final User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setGender(Gender.valueOf(registerRequest.getGender().toUpperCase()));
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setTimestamp(LocalDateTime.now());
        user.setRoles(registerRequest.getRoles().stream().map(r -> Role.valueOf(r.toUpperCase()))
                .collect(Collectors.toSet()));

        return ResponseEntity.ok(CreateResponse(userService.createUser(user)));
    }

    @PostMapping(RestConstants.Authentication.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            final User user = (User) authentication.getPrincipal();
            final String jwtToken = jwtTokenUtils.generateJwtToken(user);
            return ResponseEntity.ok(CreateResponse(new JwtTokenResponse(jwtToken, user)));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.badRequest()
                    .body(CreateResponse(false, null, Arrays.asList("The email or password is not correct")));
        }
    }

}
