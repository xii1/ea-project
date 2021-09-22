package edu.miu.cs.appointmentsystem.integration.email;

import edu.miu.cs.appointmentsystem.integration.helpers.Constants;
import edu.miu.cs.appointmentsystem.integration.models.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    private final JmsTemplate jmsTemplate;

    @Autowired
    public EmailSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void SendEmail(Email email) {
        System.out.println("Sending an email message to JMS queue. Email :" + email);
        jmsTemplate.convertAndSend(Constants.MAILBOX, email);
    }
}
