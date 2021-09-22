package edu.miu.cs.appointmentsystem.integration.email.templates;

import edu.miu.cs.appointmentsystem.integration.models.AppointmentInfo;
import edu.miu.cs.appointmentsystem.integration.models.Email;

public class AppointmentReminderTemplate implements AppointmentTemplate {

    @Override
    public Email getTemplate(AppointmentInfo appointmentInfo) {
        Email email = new Email();
        email.setSubject(appointmentInfo.getProviderName() + ": Appointment Reminder - "
                + appointmentInfo.getAppointmentDate() + " at " + appointmentInfo.getAppointmentTime());
        email.setBody(getBody(appointmentInfo));
        email.setTo(appointmentInfo.getClientEmail());
        return email;
    }

    private String getBody(AppointmentInfo appointmentInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("Dear " + appointmentInfo.getClientName() + ",");
        builder.append("This a reminder for your appointment with " + appointmentInfo.getProviderName() + " on "
                + appointmentInfo.getAppointmentDate() + " at " + appointmentInfo.getAppointmentTime());
        builder.append("\r\n");
        builder.append("Regards\r\n");
        builder.append("Appointment System Team");
        return builder.toString();
    }

}
