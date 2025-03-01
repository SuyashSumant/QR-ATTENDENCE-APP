package com.qrattendancesystem.controller;

import com.qrattendancesystem.entity.Attendance;
import com.qrattendancesystem.entity.User;
import com.qrattendancesystem.repository.AttendanceRepository;
import com.qrattendancesystem.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public AttendanceController(AttendanceRepository attendanceRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    // Check-in API
    @PostMapping("/checkin/{userId}")
    public ResponseEntity<String> checkIn(@PathVariable int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Attendance attendance = new Attendance();
        attendance.setUser(user.get());
        attendance.setCheckIn(LocalDateTime.now());

        attendanceRepository.save(attendance);
        return ResponseEntity.ok("Check-in successful");
    }

    // Check-out API
    @PostMapping("/checkout/{attendanceId}")
    public ResponseEntity<String> checkOut(@PathVariable int attendanceId) {
        Optional<Attendance> attendanceRecord = attendanceRepository.findById(attendanceId);
        if (attendanceRecord.isEmpty()) {
            return ResponseEntity.badRequest().body("Attendance record not found");
        }

        Attendance attendance = attendanceRecord.get();
        if (attendance.getCheckOut() != null) {
            return ResponseEntity.badRequest().body("Already checked out");
        }

        attendance.setCheckOut(LocalDateTime.now());
        attendanceRepository.save(attendance);
        return ResponseEntity.ok("Check-out successful");
    }
}
