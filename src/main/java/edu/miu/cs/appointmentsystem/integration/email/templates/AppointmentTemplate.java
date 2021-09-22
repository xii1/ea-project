package edu.miu.cs.appointmentsystem.integration.email.templates;

import edu.miu.cs.appointmentsystem.integration.models.AppointmentInfo;
import edu.miu.cs.appointmentsystem.integration.models.Email;

public interface AppointmentTemplate {
    Email getTemplate(AppointmentInfo appointmentInfo);

}
