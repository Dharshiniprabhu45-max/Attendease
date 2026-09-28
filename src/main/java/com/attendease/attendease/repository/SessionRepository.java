package com.attendease.attendease.repository;

import com.attendease.attendease.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findBySubject_Id(Long subjectId);

    List<Session> findBySessionDate(LocalDate sessionDate);
}
