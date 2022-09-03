package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Subject;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query("select s from Subject s where s.id = ?1")
    Subject findSubjectById (Integer id);

    @Query("select (count(s) > 0) from Subject s where s.id = ?1")
    boolean existsById(Integer id);

    @Query(nativeQuery = true, value = "SELECT * FROM subject")
    Set<Subject> getAllSubjects();

    @Query(nativeQuery = true, value = "SELECT * FROM subject WHERE name = ?1")
    Subject findByName(String subjectName);

    Set<Subject> findBySemester(Integer semester);
}
