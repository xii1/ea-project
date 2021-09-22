package edu.miu.cs.appointmentsystem.domain;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import edu.miu.cs.appointmentsystem.domain.base.BaseEntity;
import edu.miu.cs.appointmentsystem.domain.helpers.Constants;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = Constants.Category.TABLE_NAME)
public class Category extends BaseEntity {
    @Column(nullable = false, length = 200, unique = true)
    private String name;
    @Column(nullable = false)
    private LocalTime defaultDuration;
}
