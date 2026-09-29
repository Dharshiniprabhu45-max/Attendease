package com.attendease.attendease.service;

import com.attendease.attendease.entity.AttendanceRecord;
import com.attendease.attendease.entity.Student;
import com.attendease.attendease.repository.AttendanceRecordRepository;
import com.attendease.attendease.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    public StudentService(
            StudentRepository studentRepository,
            AttendanceRecordRepository attendanceRecordRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with ID: " + id));
    }

    public Student createStudent(Student student) {
        if (studentRepository.existsByRollNumber(student.getRollNumber())) {
            throw new RuntimeException("Roll number already exists");
        }

        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        Student existingStudent = getStudentById(id);

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setEmail(updatedStudent.getEmail());

        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        Student student = getStudentById(id);

        // Find all attendance records for this student
        List<AttendanceRecord> attendanceRecords =
                attendanceRecordRepository.findByStudent_Id(id);

        // Delete attendance records first
        attendanceRecordRepository.deleteAll(attendanceRecords);

        // Then delete the student
        studentRepository.delete(student);
    }
}