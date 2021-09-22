package edu.miu.cs.appointmentsystem.domain;

import javax.persistence.*;

import edu.miu.cs.appointmentsystem.domain.base.BaseEntity;
import edu.miu.cs.appointmentsystem.domain.helpers.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = Constants.Provider.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Provider extends BaseEntity {
    private String name;

    @OneToMany(cascade=CascadeType.ALL)
    Collection<User> users = new ArrayList<>();

    @OneToMany(mappedBy="provider", cascade = CascadeType.ALL)
    private Collection<Appointment> appointments = new ArrayList<>();

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(users);
    }

    public void addToUserCollection(User user){
        users.add(user);
    }
    public void removeUserFromCollection(User user){
        users.remove(user);
    }
}
