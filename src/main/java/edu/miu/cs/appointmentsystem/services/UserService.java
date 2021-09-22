package edu.miu.cs.appointmentsystem.services;

import org.springframework.security.core.userdetails.UserDetails;

import edu.miu.cs.appointmentsystem.domain.User;

import java.util.List;

public interface UserService {
    UserDetails loadUserDetails();

    User loadUserByEmail(String email);

    User getCurrentUser();

    boolean checkExists(String email);

    User createUser(User user);

    User getUserById(Long userId);

    User updateRole(User user, List<String> roles);

}
