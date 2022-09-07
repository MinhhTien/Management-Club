package fcode.backend.management.repository;

import fcode.backend.management.model.dto.LoginUserDTO;
import fcode.backend.management.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    @Query(nativeQuery = true,value = "SELECT IF(school_mail = ?1,school_mail,personal_mail) FROM member WHERE school_mail = ?1 OR personal_mail = ?1")
    String existsByEmail(String email);
    @Query("select new fcode.backend.management.model.dto.LoginUserDTO(m.id,m.role,m.ip) from Member m where m.schoolMail = ?1 or m.personalMail = ?1")
    LoginUserDTO getLoginUserByEmail(String email);
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE member set ip = ?1 where school_mail = ?2 or personal_mail = ?2")
    int updateIpByEmail(String ip,String email);
}
