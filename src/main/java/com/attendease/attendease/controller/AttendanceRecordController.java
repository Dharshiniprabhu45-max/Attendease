
package com.attendease.attendease.controller;

import com.attendease.attendease.entity.AttendanceRecord;
import com.attendease.attendease.service.AttendanceRecordService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceRecordController {

    private final AttendanceRecordService attendanceService;

    public AttendanceRecordController(
            AttendanceRecordService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Get all attendance records
    @GetMapping
    public List<AttendanceRecord> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    // Get attendance by ID
    @GetMapping("/{id}")
    public AttendanceRecord getAttendanceById(
            @PathVariable Long id) {
        return attendanceService.getAttendanceById(id);
    }

    // Get attendance by student
    @GetMapping("/student/{studentId}")
    public List<AttendanceRecord> getAttendanceByStudent(
            @PathVariable Long studentId) {
        return attendanceService.getAttendanceByStudent(studentId);
    }

    // Get attendance by session
    @GetMapping("/session/{sessionId}")
    public List<AttendanceRecord> getAttendanceBySession(
            @PathVariable Long sessionId) {
        return attendanceService.getAttendanceBySession(sessionId);
    }

    // Calculate attendance percentage
    @GetMapping("/student/{studentId}/percentage")
    public double getAttendancePercentage(
            @PathVariable Long studentId) {
        return attendanceService.calculateAttendancePercentage(studentId);
    }

    // Check attendance threshold
    @GetMapping("/student/{studentId}/alert")
    public String checkAttendanceThreshold(
            @PathVariable Long studentId) {
        return attendanceService.checkAttendanceThreshold(studentId);
    }

    // Get attendance summary
    @GetMapping("/student/{studentId}/summary")
    public Map<String, Object> getAttendanceSummary(
            @PathVariable Long studentId) {
        return attendanceService.getAttendanceSummary(studentId);
    }

    // Mark attendance
    @PostMapping
    public AttendanceRecord markAttendance(
            @RequestBody AttendanceRecord record) {
        return attendanceService.markAttendance(record);
    }

    // Update attendance
    @PutMapping("/{id}")
    public AttendanceRecord updateAttendance(
            @PathVariable Long id,
            @RequestParam boolean present) {
        return attendanceService.updateAttendance(id, present);
    }

    // Delete attendance
    @DeleteMapping("/{id}")
    public String deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return "Attendance record deleted successfully";
    }
}