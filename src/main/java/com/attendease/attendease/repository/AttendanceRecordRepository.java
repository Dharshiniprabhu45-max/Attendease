package com.attendease.attendease.repository;

import com.attendease.attendease.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRecordRepository
        extends JpaRepository<AttendanceRecord, Long> {

    List<AttendanceRecord> findByStudent_Id(Long studentId);

    List<AttendanceRecord> findBySession_Id(Long sessionId);

    boolean existsByStudent_IdAndSession_Id(
            Long studentId, Long sessionId);
}
