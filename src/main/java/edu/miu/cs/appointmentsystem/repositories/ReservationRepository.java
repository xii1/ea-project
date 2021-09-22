package edu.miu.cs.appointmentsystem.repositories;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.miu.cs.appointmentsystem.common.enums.ReservationStatus;
import edu.miu.cs.appointmentsystem.domain.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = "SELECT distinct r FROM Appointment as a join  a.reservations as r where a.id = :id and a.provider.id =:providerId ", countQuery = "SELECT count(r) FROM Appointment as a join  a.reservations as r where a.id = :id")
    Page<Reservation> findByAppointmentIdForProvider(Long id, Long providerId, Pageable pageable);

    @Query(value = "SELECT distinct r FROM Appointment as a join  a.reservations as r where a.id = :id and r.createdBy.id=:userId ", countQuery = "SELECT count(r) FROM Appointment as a join  a.reservations as r where a.id = :id")
    Page<Reservation> findByAppointmentIdForClient(Long id, Long userId, Pageable pageable);

    @Query(value = "SELECT count(r) FROM Appointment as a join  a.reservations as r where a.id = :appointmentId and r.createdBy.id =:userId and r.status =:status")
    Integer countByUserIdAndStatus(Long appointmentId, Long userId, ReservationStatus status);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query(value = "SELECT r FROM Reservation as r where r.id =:id")
    Optional<Reservation> findByIdForUpdate(Long id);
}
