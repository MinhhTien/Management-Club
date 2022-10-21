package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Integer> {
    @Query(nativeQuery = true, value = "SELECT * from register")
    List<Register> getAllRegisters();

    Register findRegisterById(Integer id);

    boolean existsById(Integer id);

    Register findRegisterByStudentId(String studentId);

    boolean existsByStudentId(String studentId);

    boolean existsByPersonalMail(String personalMail);

    boolean existsBySchoolMail(String schoolMail);

    @Query(nativeQuery = true, value = "SELECT * FROM register where personal_mail = ?1 OR school_mail = ?2")
    Register findByPersonalMailOrSchoolMail(String personalMail, String schoolMail);
}
