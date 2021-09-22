package edu.miu.cs.appointmentsystem.integration.email.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import edu.miu.cs.appointmentsystem.integration.email.templates.AppointmentReminderTemplate;
import edu.miu.cs.appointmentsystem.integration.email.templates.AppointmentTemplate;
import edu.miu.cs.appointmentsystem.integration.helpers.Constants;
import edu.miu.cs.appointmentsystem.integration.models.AppointmentInfo;
import edu.miu.cs.appointmentsystem.resources.AppointmentReminderConfiguration;
import edu.miu.cs.appointmentsystem.services.AppointmentService;

@Component
public class EmailSchedulerJmsListener {
    ScheduledExecutorService scheduleTool = Executors.newScheduledThreadPool(5);
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AppointmentReminderConfiguration appointmentReminderConfiguration;
    private int startFetchFromPage = 0;

    @JmsListener(destination = Constants.APPOINTMENTS_LOAD, containerFactory = Constants.systemJmsFactory)
    public void loadAndScheduleAppointments(String name) {
        Page<AppointmentInfo> appointments;
        do {

            appointments = appointmentService.loadAppointmentsForReminder(startFetchFromPage,
                    appointmentReminderConfiguration.getBatchSize());
            if (appointments.getSize() > 0) {

                appointments.getContent().forEach(scheduleAppointmentReminderConsumer);
            }
            startFetchFromPage += 1;
        } while (appointments.getTotalPages() > startFetchFromPage);
    }

    private Consumer<? super AppointmentInfo> scheduleAppointmentReminderConsumer = (appointmentInfo) -> {

        scheduleAppointmentReminder(appointmentInfo);
    };

    @JmsListener(destination = Constants.SCHEDULE_NEW_RESERVATION_REMINDERS, containerFactory = Constants.systemJmsFactory)
    private void scheduleAppointmentReminder(AppointmentInfo appointmentInfo) {
        LocalDateTime appointmentTime = LocalDateTime.of(appointmentInfo.getAppointmentDate(),
                appointmentInfo.getAppointmentTime());

        ZonedDateTime firstZoneDateTime = appointmentTime
                .minusHours(appointmentReminderConfiguration.getFirstReminder()).atZone(ZoneId.systemDefault());

        Long fistDelay = firstZoneDateTime.toEpochSecond();
        ZonedDateTime secondZoneDateTime = appointmentTime
                .minusHours(appointmentReminderConfiguration.getSecondReminder()).atZone(ZoneId.systemDefault());

        Long secondDelay = secondZoneDateTime.toEpochSecond();
        if (fistDelay > 0) {
            appointmentInfo.setFirstReminder(true);
            appointmentInfo.setSecondReminder(false);
            scheduleAppointmentReminder(new AppointmentReminderTemplate(), appointmentInfo, fistDelay);
        }
        if (secondDelay > 0) {
            appointmentInfo.setFirstReminder(false);
            appointmentInfo.setSecondReminder(true);
            scheduleAppointmentReminder(new AppointmentReminderTemplate(), appointmentInfo, secondDelay);
        }
    }

    @Async
    public void scheduleAppointmentReminder(AppointmentTemplate template, AppointmentInfo appointmentInfo, Long delay) {
        EmailSendTask task = new EmailSendTask(template.getTemplate(appointmentInfo), appointmentInfo);
        scheduleTool.schedule(task, delay, TimeUnit.SECONDS);
    }

    @PreDestroy
    private void cleanUp() {
        scheduleTool.shutdown();
    }
}
