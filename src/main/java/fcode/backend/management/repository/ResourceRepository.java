package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Resource;
import fcode.backend.management.repository.entity.Subject;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    Resource findOneById(Integer resourceId);

    Resource findResourceByUrl(String url);

    @Query("select (count(r) > 0) from Resource r where r.subject.id = ?1")
    Boolean existsResourceBySubject(Integer subjectId);

    @Query(nativeQuery = true, value = "SELECT subject_id FROM resource WHERE id = ?1")
    Integer findSubjectId(Integer resourceId);

    @Query(nativeQuery = true, value = "SELECT resource.id, resource.url, resource.contributor, resource.description, resource.subject_id FROM resource INNER JOIN subject ON resource.subject_id = subject.id ORDER BY subject.semester ASC, subject.name ASC ")
    Set<Resource> getAllResources();

    @Query("select r from Resource r where r.subject.id = ?1")
    Set<Resource> getResourcesBySubjectId(Integer subjectId);

    @Query(nativeQuery = true, value = "SELECT resource.id, resource.url, resource.contributor, resource.description, resource.subject_id FROM resource INNER JOIN subject ON resource.subject_id = subject.id WHERE subject.semester = ?1 ORDER BY subject.name ASC ")
    Set<Resource> getResourcesBySubjectSemester(Integer semester);

    boolean existsByUrl(String resourceUrl);
    boolean existsById(Integer id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM resource WHERE id = ?1")
    void deleteById(Integer id);
}
