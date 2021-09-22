package edu.miu.cs.appointmentsystem.integration.email.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import edu.miu.cs.appointmentsystem.integration.helpers.Constants;

@Component
public class EmailScheduler {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void LoadAllAppointment() {
        jmsTemplate.convertAndSend(Constants.APPOINTMENTS_LOAD);
    }
}
