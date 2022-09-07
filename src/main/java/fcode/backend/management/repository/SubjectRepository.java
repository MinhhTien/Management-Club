package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    Subject findSubjectById(Integer id);
    Subject findByName(String subjectName);
    List<Subject> findBySemester(Integer semester);

    boolean existsById(Integer id);

    @Query(nativeQuery = true, value = "SELECT * FROM subject ORDER BY semester ASC ")
    List<Subject> getAllSubjects();

    @Query(nativeQuery = true, value = "SELECT * FROM subject WHERE LOWER(name) LIKE LOWER(?1)")
    List<Subject> searchAllByName(String value);
}
