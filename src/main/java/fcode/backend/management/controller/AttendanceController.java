package fcode.backend.management.controller;

import fcode.backend.management.model.dto.AttendanceDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;

    @GetMapping(value = "/all")
    public Response<List<AttendanceDTO>> getAllAttendance() {
        return attendanceService.getAllAttendances();
    }

    @GetMapping(value = "/eventId/{eventId}")
    public Response<List<AttendanceDTO>> getAttendanceById(@PathVariable Integer eventId) {
        return attendanceService.getAttendancesByEventId(eventId);
    }

    @GetMapping(value = "/studentId/{studentId}")
    public Response<List<AttendanceDTO>> getAttendanceByStudentId(@PathVariable String studentId) {
        return attendanceService.getAttendancesByStudentID(studentId);
    }

    @GetMapping
    public Response<List<AttendanceDTO>> getAttendancesByUserId(@RequestAttribute(required = false) Integer userId) {
        return attendanceService.getAttendancesByMemberId(userId);
    }

    @PostMapping(value = "/new")
    public Response<Void> createAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.createAttendance(attendanceDTO);
    }

    @PutMapping
    public Response<Void> updateAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.updateAttendance(attendanceDTO);
    }

    @DeleteMapping(value = "/{id}")
    public Response<Void> deleteAttendance(@PathVariable Integer id) {
        return attendanceService.deleteAttendance(id);
    }
}
