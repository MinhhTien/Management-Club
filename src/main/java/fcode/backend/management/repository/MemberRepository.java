package fcode.backend.management.repository;

import fcode.backend.management.model.dto.EmailReceiverDTO;
import fcode.backend.management.model.dto.LoginUserDTO;
import fcode.backend.management.repository.entity.Crew;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.repository.entity.Position;
import fcode.backend.management.service.constant.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    @Query(nativeQuery = true, value = "SELECT * from  member WHERE status = ?1")
    List<Member> findALlMember(String status);

    @Query(nativeQuery = true, value = "SELECT * from member WHERE student_id = ?1 AND status = ?2")
    Member findMemberByStudentId(String studentId, String status);

    @Query(nativeQuery = true, value = "SELECT * from member WHERE last_name = ?1 AND status = ?2")
    List<Member> findMemberByLastname(String lastname, String status);

    @Query
    boolean existsByLastNameAndStatus(String lastName, Status status);

    Member findMemberById(Integer id);

    Member findMemberByIdAndStatus(Integer id, Status status);

    @Query(nativeQuery = true, value = "SELECT * FROM member WHERE school_mail = ?1 OR personal_mail = ?1")
    Member findMemberByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT IF(school_mail = ?1,school_mail,personal_mail) FROM member WHERE school_mail = ?1 OR personal_mail = ?1")
    String existsByEmail(String email);

    @Query("select new fcode.backend.management.model.dto.LoginUserDTO(m.id,m.role,m.ip) from Member m where m.schoolMail = ?1 or m.personalMail = ?1")
    LoginUserDTO getLoginUserByEmail(String email);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE member set ip = ?1 where school_mail = ?2 or personal_mail = ?2")
    int updateIpByEmail(String ip, String email);

    @Query(nativeQuery = true, value = "SELECT IFNULL(school_mail, personal_mail) from member WHERE id = ?1 AND status = ?2")
    String getEmailById(Integer id, String status);

    @Query(nativeQuery = true, value = "SELECT IFNULL(school_mail, personal_mail) from member WHERE crew_id = ?1 AND status = ?2")
    List<String> getEmailsByCrewId(Integer crewId, String status);

    @Query(nativeQuery = true, value = "SELECT IFNULL(school_mail, personal_mail) from member WHERE member.student_id LIKE(?1) AND status = ?2")
    List<String> getEmailsByK(String K, String status);

    @Query("select new fcode.backend.management.model.dto.EmailReceiverDTO(m.studentId,m.firstName, m.lastName) from Member m where m.schoolMail = ?1 or m.personalMail = ?1 and m.status = ?2")
    EmailReceiverDTO getReceiverByEmail(String email, Status status);

    boolean existsByCrew(Crew crew);

    @Query(nativeQuery = true, value = "SELECT * from member where position_id = ?1 AND status =?2")
    List<Member> getMembersByPositionIdAndStatus(Integer id, String status);

    boolean existsByPosition(Position position);
    @Query(nativeQuery = true, value = "SELECT * FROM fcode_management.member where verification_code = ?1")
    Member findByVerificationCode(String verificationCode);

    
    boolean existsByStudentId(String studentId);
} 

