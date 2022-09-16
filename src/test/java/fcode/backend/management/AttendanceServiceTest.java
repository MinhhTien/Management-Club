package fcode.backend.management;

import fcode.backend.management.config.interceptor.State;
import fcode.backend.management.model.dto.AttendanceDTO;
import fcode.backend.management.repository.AttendanceRepository;
import fcode.backend.management.repository.entity.Attendance;
import fcode.backend.management.repository.entity.Event;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.service.AttendanceService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class AttendanceServiceTest {
    @Autowired
    AttendanceService attendanceService;

    @Autowired
    AttendanceRepository attendanceRepository;

//    @Test
//    void getAllAttendance() {
//        List<AttendanceDTO> attendanceDTOList = attendanceService.getAllAttendances().getData();
//        attendanceDTOList.forEach(System.out::println);
//    }
//
//    @Test
//    void getAttendancesByStudentID() {
//        List<AttendanceDTO> attendanceDTOList = attendanceService.getAttendancesByStudentID("SE160411").getData();
//        attendanceDTOList.forEach(System.out::println);
//    }
//
//    @Test
//    void getAttendancesByEventID() {
//        List<AttendanceDTO> attendanceDTOList = attendanceService.getAttendancesByEventId(1).getData();
//        attendanceDTOList.forEach(System.out::println);
//    }
//
//    @Test
//    void getAttendancesByMemberID() {
//        List<AttendanceDTO> attendanceDTOList = attendanceService.getAttendancesByMemberId(2).getData();
//        attendanceDTOList.forEach(System.out::println);
//    }
//
//    @Test
//    void createAttendance() {
//        attendanceService.createAttendance(new AttendanceDTO(1, 2, new Date(), State.ON_TIME));
//        attendanceService.createAttendance(new AttendanceDTO(2, 2, new Date(), State.LATE));
//    }
//
//    @Test
//    void updateAttendance() {
//        attendanceService.updateAttendance(new AttendanceDTO(6, 2, 2, null, State.ON_TIME));
//    }
//
//    @Test
//    void deleteAttendance() {
//        attendanceService.deleteAttendance(6);
//    }

}
