package edu.miu.cs.appointmentsystem.domain.base;

import java.time.LocalDateTime;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.miu.cs.appointmentsystem.domain.User;
import edu.miu.cs.appointmentsystem.domain.helpers.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Version;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @AccessType(AccessType.Type.PROPERTY)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Constants.BaseEntity.CREATED_BY_ID, nullable = true)
    private User createdBy;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Constants.BaseEntity.UPDATED_BY_ID, nullable = true)
    private User updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    @Version
    private LocalDateTime timestamp;
}
