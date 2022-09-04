package fcode.backend.management.repository;

import fcode.backend.management.model.dto.LoginUserDTO;
import fcode.backend.management.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    @Query(nativeQuery = true,value = "SELECT IF(school_email = ?1,school_email,person_email) FROM member WHERE school_email = ?1 OR personal_email = ?1")
    String existsByEmail(String email);
    @Query("select new fcode.backend.management.model.dto.LoginUserDTO(m.id,m.role,m.ip) from Member m where m.schoolMail = ?1 or personalMail = ?1")
    LoginUserDTO getLoginUserByEmail(String email);
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE member set member.ip = ?1 where member.email = ?2")
    int updateIpByEmail(String ip,String email);
}
