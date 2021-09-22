package edu.miu.cs.appointmentsystem.repositories;

import edu.miu.cs.appointmentsystem.domain.EmailServiceLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailServiceLogRepository extends JpaRepository<EmailServiceLog, Integer> {
}
