package edu.miu.cs.appointmentsystem.services;

import edu.miu.cs.appointmentsystem.domain.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import edu.miu.cs.appointmentsystem.domain.User;
import edu.miu.cs.appointmentsystem.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) authentication.getPrincipal();
    }

    @Override
    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User getCurrentUser() {
        UserDetails userDetails = loadUserDetails();
        return loadUserByEmail(userDetails.getUsername());
    }

    @Override
    public boolean checkExists(String email) {
        final User user = userRepository.findByEmail(email).orElse(null);
        if (user != null)
            return true;
        else
            return false;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User updateRole(User user, List<String> roles) {
        if (roles == null || roles.isEmpty())
            return user;

        final Set<Role> newRoles = Arrays.stream(Role.class.getEnumConstants())
                .filter(r -> roles.contains(r.getValue())).collect(Collectors.toSet());

        if (!newRoles.isEmpty()) {
            user.setRoles(newRoles);
            return userRepository.save(user);
        }

        return user;
    }

}
