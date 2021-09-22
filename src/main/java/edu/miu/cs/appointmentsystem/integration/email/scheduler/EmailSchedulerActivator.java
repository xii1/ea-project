package edu.miu.cs.appointmentsystem.integration.email.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import edu.miu.cs.appointmentsystem.integration.helpers.Constants;

@Component
public class EmailSchedulerActivator {
    @Autowired
    private JmsTemplate jmsTemplate;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        jmsTemplate.convertAndSend(Constants.APPOINTMENTS_LOAD, "Start");
    }
}
