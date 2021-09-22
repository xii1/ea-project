package edu.miu.cs.appointmentsystem.integration.email;

import edu.miu.cs.appointmentsystem.common.enums.EmailSendStatus;
import edu.miu.cs.appointmentsystem.domain.EmailServiceLog;
import edu.miu.cs.appointmentsystem.integration.helpers.Constants;
import edu.miu.cs.appointmentsystem.integration.models.Email;
import edu.miu.cs.appointmentsystem.repositories.EmailServiceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailReceiver {
    private final EmailServiceLogRepository emailServiceLogRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailReceiver(EmailServiceLogRepository emailServiceLogRepository, JavaMailSender javaMailSender) {
        this.emailServiceLogRepository = emailServiceLogRepository;
        this.javaMailSender = javaMailSender;
    }

    @JmsListener(destination = Constants.MAILBOX, containerFactory = Constants.systemJmsFactory)
    public void receiveMessage(Email email) {
        EmailServiceLog log = saveEmailServiceLog(email);
        try {
            System.out.println("Sending an email message :" + email);
            sendEmail(email);
            updateEmailServiceLog(log, EmailSendStatus.SENT);
        } catch (Exception ex) {
            System.out.println("Email sending failed");
            ex.printStackTrace();
            updateEmailServiceLog(log, EmailSendStatus.ERROR);
        }
    }

    private void sendEmail(Email email) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            
            msg.setTo(email.getTo());

            msg.setSubject(email.getSubject());
            msg.setText(email.getBody());

            javaMailSender.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private EmailServiceLog saveEmailServiceLog(Email email) {
        EmailServiceLog emailServiceLog = null;
        try {
            emailServiceLog = new EmailServiceLog();
            emailServiceLog.setId(0L);
            emailServiceLog.setReceiver(email.getTo());
            emailServiceLog.setSubject(email.getSubject());
            emailServiceLog.setBody(email.getBody());
            emailServiceLog.setNotes("Init");
            emailServiceLog.setCreatedDate(LocalDateTime.now());
            emailServiceLog.setStatus(EmailSendStatus.PENDING);
            emailServiceLog = emailServiceLogRepository.save(emailServiceLog);
        } catch (Exception ex) {
            System.out.println("Email service log insert failed");
            ex.printStackTrace();
        }

        return emailServiceLog;
    }

    private EmailServiceLog updateEmailServiceLog(EmailServiceLog emailServiceLog, EmailSendStatus newStatus) {
        try {
            emailServiceLog.setStatus(newStatus);
            emailServiceLog.setUpdateDate(LocalDateTime.now());
            emailServiceLogRepository.save(emailServiceLog);
        } catch (Exception ex) {
            System.out.println("Email service log update failed");
            ex.printStackTrace();
        }
        return emailServiceLog;
    }
}
