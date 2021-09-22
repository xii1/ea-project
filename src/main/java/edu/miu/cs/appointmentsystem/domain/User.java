package edu.miu.cs.appointmentsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.miu.cs.appointmentsystem.domain.base.BaseEntity;
import edu.miu.cs.appointmentsystem.domain.enums.Gender;
import edu.miu.cs.appointmentsystem.domain.enums.Role;
import edu.miu.cs.appointmentsystem.domain.helpers.Constants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = Constants.User.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {
    private String firstName;

    private String lastName;
    @Transient
    @Setter(value = AccessLevel.PRIVATE)
    private String fullName;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private Gender gender;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @SuppressWarnings("unchecked")
    private List<UserRole> userRoles = Collections.EMPTY_LIST;;

    public Set<Role> getRoles() {
        return userRoles.stream().map(UserRole::getRole).collect(Collectors.toSet());
    }

    public void setRoles(Set<Role> roles) {
        if (roles != null) {
            userRoles = roles.stream().map(role -> new UserRole(getId(), role)).collect(Collectors.toList());
        }
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getValue()));
        }
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
