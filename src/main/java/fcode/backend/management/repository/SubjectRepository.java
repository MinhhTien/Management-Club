package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    Subject findOneById(Integer id);

    boolean existsByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM subject")
    Set<Subject> getAllSubject();

    @Query(nativeQuery = true, value = "SELECT * FROM subject WHERE name = ?1")
    Subject findByName(String subjectName);

    Set<Subject> findBySemester(Integer semester);
}
