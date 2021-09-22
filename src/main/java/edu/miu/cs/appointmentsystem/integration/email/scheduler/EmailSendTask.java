package edu.miu.cs.appointmentsystem.integration.email.scheduler;

import org.springframework.beans.factory.annotation.Autowired;

import edu.miu.cs.appointmentsystem.integration.email.EmailSender;
import edu.miu.cs.appointmentsystem.integration.models.AppointmentInfo;
import edu.miu.cs.appointmentsystem.integration.models.Email;
import edu.miu.cs.appointmentsystem.services.AppointmentService;

public class EmailSendTask implements Runnable {
    @Autowired
    private EmailSender emailSender;
    private Email email;
    private AppointmentInfo appointmentInfo;
    @Autowired
    private AppointmentService appointmentService;

    public EmailSendTask(Email email, AppointmentInfo appointmentInfo) {
        this.email = email;
        this.appointmentInfo = appointmentInfo;
    }

    public EmailSendTask(Email email) {
        this.email = email;
    }

    @Override
    public void run() {

        emailSender.SendEmail(email);
        updateAppointment();
    }

    private void updateAppointment() {
        if (appointmentInfo != null && appointmentInfo.isFirstReminder()) {
            appointmentService.updateFirstSend(appointmentInfo.getId());
        }
        if (appointmentInfo != null && appointmentInfo.isSecondReminder()) {
            appointmentService.updateSecondSend(appointmentInfo.getId());
        }
    }
}
