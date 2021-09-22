package edu.miu.cs.appointmentsystem.repositories;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.miu.cs.appointmentsystem.domain.Appointment;
import edu.miu.cs.appointmentsystem.integration.models.AppointmentInfo;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query(value = "SELECT  a FROM Appointment as a  where (a.category.id=:category_id or :category_id=0) and  (a.provider.id=:provider_id or :provider_id=0) and exists(select p.id FROM Provider as p  join p.users as u where u.id=:userId )", countQuery = "SELECT  COUNT(a.id) FROM Appointment as a  where (a.category.id=:category_id or :category_id=0) and  (a.provider.id=:provider_id or :provider_id=0) and exists(select p.id FROM Provider as p  join p.users as u where u.id=:userId )")
    Page<Appointment> findAllByCategoryAndProviderForProvider(Long category_id, Long provider_id, Long userId,
            Pageable pageable);

    @Query(value = "SELECT distinct  a FROM Appointment as a  join a.reservations  as r where (a.category.id=:category_id or :category_id=0) and  (a.provider.id=:provider_id or :provider_id=0) and r.createdBy.id =:userId", countQuery = "SELECT  COUNT(distinct a.id)  FROM Appointment as a  join  a.reservations  as r where (a.category.id=:category_id or :category_id=0) and  (a.provider.id=:provider_id or :provider_id=0) and r.createdBy.id =:userId")
    Page<Appointment> findAllByCategoryAndProviderForClient(Long category_id, Long provider_id, Long userId,
            Pageable pageable);

    @Query(value = "SELECT  new edu.miu.cs.appointmentsystem.integration.models.AppointmentInfo(a.id,a.provider.name,a.date,a.time,a.duration,a.location,r.createdBy.firstName+' ' +r.createdBy.lastName, r.createdBy.email,r.status,r.note)  FROM Appointment as a  join  a.reservations as r  WHERE a.available=0 and a.firstReminderSent=0 and a.secondReminderSent=0 and r.status=1 and a.date >= current_date()")
    Page<AppointmentInfo> loadAppointmentInfosForReminders(Pageable pageable);

    @Query("update Appointment set firstReminderSent=true where id=:id")
    void updateFirstReminder(long id);

    @Query("update Appointment set secondReminderSent=true where id=:id")
    void updateSecondReminder(long id);

    @Query(value = "SELECT distinct a FROM Appointment as a  join  a.reservations r  where a.id =:appointmentId and a.provider.id=:providerId and r.createdBy=:userId")
    Optional<Appointment> findByProviderIdAndAppointmentIdAndUserId(Long providerId, Long appointmentId, Long userId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query(value = "SELECT a FROM Appointment as a where a.id =:id")
    Optional<Appointment> findByIdForUpdate(Long id);

}
