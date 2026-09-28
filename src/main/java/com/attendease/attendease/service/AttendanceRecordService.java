

package com.attendease.attendease.service;

import com.attendease.attendease.entity.AttendanceRecord;
import com.attendease.attendease.repository.AttendanceRecordRepository;
import com.attendease.attendease.repository.StudentRepository;
import com.attendease.attendease.repository.SessionRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AttendanceRecordService {

    private final AttendanceRecordRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SessionRepository sessionRepository;

    public AttendanceRecordService(
            AttendanceRecordRepository attendanceRepository,
            StudentRepository studentRepository,
            SessionRepository sessionRepository) {

        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.sessionRepository = sessionRepository;
    }

    // Get all attendance records
    public List<AttendanceRecord> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    // Get attendance by ID
    public AttendanceRecord getAttendanceById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Attendance record not found: " + id));
    }

    // Get attendance by student
    public List<AttendanceRecord> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudent_Id(studentId);
    }

    // Get attendance by session
    public List<AttendanceRecord> getAttendanceBySession(Long sessionId) {
        return attendanceRepository.findBySession_Id(sessionId);
    }

    // Mark attendance
    public AttendanceRecord markAttendance(AttendanceRecord record) {

        if (record.getStudent() == null
                || record.getStudent().getId() == null) {
            throw new RuntimeException("Student ID is required");
        }

        if (record.getSession() == null
                || record.getSession().getId() == null) {
            throw new RuntimeException("Session ID is required");
        }

        Long studentId = record.getStudent().getId();
        Long sessionId = record.getSession().getId();

        if (attendanceRepository
                .existsByStudent_IdAndSession_Id(studentId, sessionId)) {
            throw new RuntimeException(
                    "Attendance already marked for this student and session");
        }

        var student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found: " + studentId));

        var session = sessionRepository.findById(sessionId)
                .orElseThrow(() ->
                        new RuntimeException("Session not found: " + sessionId));

        record.setStudent(student);
        record.setSession(session);

        return attendanceRepository.save(record);
    }

    // Update attendance
    public AttendanceRecord updateAttendance(Long id, boolean present) {
        AttendanceRecord record = getAttendanceById(id);
        record.setPresent(present);
        return attendanceRepository.save(record);
    }

    // Delete attendance
    public void deleteAttendance(Long id) {
        AttendanceRecord record = getAttendanceById(id);
        attendanceRepository.delete(record);
    }

    // Calculate attendance percentage
    public double calculateAttendancePercentage(Long studentId) {

        List<AttendanceRecord> records =
                attendanceRepository.findByStudent_Id(studentId);

        if (records.isEmpty()) {
            return 0.0;
        }

        long totalClasses = records.size();

        long attendedClasses = records.stream()
            .filter(record -> record != null && record.isPresent())
                .count();

        return (attendedClasses * 100.0) / totalClasses;
    }

    // Check attendance threshold (100%)
    public String checkAttendanceThreshold(Long studentId) {

        double percentage = calculateAttendancePercentage(studentId);

        double threshold = 100.0;

        if (percentage < threshold) {
            return "ALERT: Attendance is below 100%. Current attendance: "
                    + percentage + "%";
        }

        return "Attendance requirement met. Current attendance: "
                + percentage + "%";
    }

    // Generate attendance summary
    public Map<String, Object> getAttendanceSummary(Long studentId) {

        List<AttendanceRecord> records =
                attendanceRepository.findByStudent_Id(studentId);

        long totalClasses = records.size();

        long classesAttended = records.stream()
            .filter(record -> record != null && record.isPresent())
                .count();

        long classesAbsent = totalClasses - classesAttended;

        double percentage = totalClasses == 0
                ? 0.0
                : (classesAttended * 100.0) / totalClasses;

        String status = percentage < 100.0
                ? "BELOW_THRESHOLD"
                : "REQUIREMENT_MET";

        Map<String, Object> summary = new HashMap<>();

        summary.put("studentId", studentId);
        summary.put("totalClasses", totalClasses);
        summary.put("classesAttended", classesAttended);
        summary.put("classesAbsent", classesAbsent);
        summary.put("attendancePercentage", percentage);
        summary.put("status", status);

        return summary;
    }
}