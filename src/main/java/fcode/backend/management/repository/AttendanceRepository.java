package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    @Query(nativeQuery = true, value = "SELECT * from attendance")
    List<Attendance> findAllAttendances();

    @Query(nativeQuery = true, value = "SELECT * from attendance WHERE id = ?1")
    Attendance getAttendanceById(Integer id);

    @Query(nativeQuery = true, value = "SELECT * FROM fcode_management.attendance where member_id = ?1 and event_id = ?2")
    Attendance existsByMemberIdAndEventId(Integer memberId, Integer eventId);

    @Query(nativeQuery = true, value = "SELECT attendance.id, member.id as member_id, member.student_id, member.last_name, event.id as event_id, event.name as event_name, attendance.date, attendance.state\n" +
            "FROM ((fcode_management.attendance inner join fcode_management.member on attendance.member_id = member.id)\n" +
            "inner join fcode_management.event on attendance.event_id = event.id) where student_id = ?1 order by attendance.id asc")
    List<Attendance> getAttendancesByStudentId(String studentId);

    @Query(nativeQuery = true, value = "SELECT attendance.id, event_id, event.name, member_id, member.student_id, member.last_name, attendance.date, attendance.state\n" +
            "FROM ((fcode_management.attendance inner join fcode_management.member on attendance.member_id = member.id)\n" +
            "inner join fcode_management.event on attendance.event_id = event.id) where event_id = ?1 order by attendance.id asc")
    List<Attendance> getAttendancesByEventId(Integer eventId);

    @Query(nativeQuery = true, value = "SELECT attendance.id, event_id, event.name, member_id, member.student_id, member.last_name, attendance.date, attendance.state\n" +
            "FROM ((fcode_management.attendance inner join fcode_management.member on attendance.member_id = member.id)\n" +
            "inner join fcode_management.event on attendance.event_id = event.id) where member_id = ?1 order by attendance.id asc")
    List<Attendance> getAttendancesByMemberId(Integer memberId);

    @Query(nativeQuery = true, value = "SELECT IFNULL(school_mail, personal_mail) FROM member INNER JOIN attendance ON member.id = attendance.member_id WHERE event_id = ?1 ")
    List<String> getEmailsByEventId(Integer eventId);

    @Query(nativeQuery = true, value = "SELECT * FROM fcode_management.attendance WHERE attendance.member_id = ?1 AND attendance.date BETWEEN ?2 AND ?3")
    List<Attendance> getByMemberIdBetweenTime(Integer memberId, Date startDate, Date endDate);
}
