package edu.miu.cs.appointmentsystem.domain;

import javax.persistence.*;

import edu.miu.cs.appointmentsystem.common.enums.EmailSendStatus;
import edu.miu.cs.appointmentsystem.domain.helpers.Constants;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = Constants.EmailServiceLog.TABLE_NAME)
@Getter
@Setter
public class EmailServiceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public EmailServiceLog() {
        status = EmailSendStatus.PENDING;
    }

    @Column(nullable = false)
    private String receiver;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false)
    @Lob
    private String body;
    @Column(nullable = false)
    private EmailSendStatus status;
    private String notes;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
