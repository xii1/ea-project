package edu.miu.cs.appointmentsystem.integration.email.templates;

import edu.miu.cs.appointmentsystem.integration.email.templates.enums.EmailTemplateFor;
import edu.miu.cs.appointmentsystem.integration.models.AppointmentInfo;
import edu.miu.cs.appointmentsystem.integration.models.Email;

public class AppointmentReservationTemplate implements AppointmentTemplate {
    private EmailTemplateFor emailTemplateFor;

    public AppointmentReservationTemplate(EmailTemplateFor emailTemplateFor) {
        this.emailTemplateFor = emailTemplateFor;
    }

    @Override
    public Email getTemplate(AppointmentInfo appointmentInfo) {
        Email email = new Email();
        email.setSubject(appointmentInfo.getProviderName() + ": IMPORTANT : Appointment reservation has been PLACED - "
                + appointmentInfo.getAppointmentDate() + " at " + appointmentInfo.getAppointmentTime());
        email.setBody(getBody(appointmentInfo));
        email.setTo(emailTemplateFor == EmailTemplateFor.Client ? appointmentInfo.getClientEmail()
                : appointmentInfo.getProviderEmail());
        return email;
    }

    private String getBody(AppointmentInfo appointmentInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("Dear " + (emailTemplateFor == EmailTemplateFor.Client ? appointmentInfo.getClientName()
                : appointmentInfo.getProviderName()) + ",");
        if (emailTemplateFor == EmailTemplateFor.Client) {
            builder.append("We would like let you know the your appointment with " + appointmentInfo.getProviderName()
                    + " on " + appointmentInfo.getAppointmentDate() + " at " + appointmentInfo.getAppointmentTime());
            builder.append(" has been PLACE, WE WILL LET YOU KNOW ONCE ACCEPTED.");
        } else {
            builder.append("A client book a reservation on one of your appointment with with name : "
                    + appointmentInfo.getClientName() + " on " + appointmentInfo.getAppointmentDate() + " at "
                    + appointmentInfo.getAppointmentTime());
            builder.append(".");
        }

        builder.append("Regards");
        builder.append("Appointment System Team");
        return builder.toString();
    }

}
