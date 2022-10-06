package fcode.backend.management;

import fcode.backend.management.repository.AttendanceRepository;
import fcode.backend.management.repository.entity.Attendance;
import fcode.backend.management.service.AttendanceService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
//        attendanceService.createAttendance(new AttendanceDTO(2, 2, new Date(), State.ON_TIME));
//        attendanceService.createAttendance(new AttendanceDTO(2, 1, new Date(), State.LATE));
//        attendanceService.createAttendance(new AttendanceDTO(2, 3, new Date(), State.LATE));
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

    String sDate1 = "01/09/2022";
    String sDate2 = "30/09/2022";
    Date date1;
    Date date2;

    {
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
            date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getByMemberIdBetweenTime() {
        List<Attendance> attendanceList = attendanceRepository.getByMemberIdBetweenTime(1, date1, date2);
            for(Attendance attendance : attendanceList) {
                attendance.toString();
            }
    }
}
