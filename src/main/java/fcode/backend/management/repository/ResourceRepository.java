package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    Resource findOneById(Integer resourceId);

    @Query("select (count(r) > 0) from Resource r where r.subject.id = ?1")
    boolean existsBySubject(Integer subjectId);
    boolean existsById(Integer id);
    boolean existsByUrl(String url);

    @Query(nativeQuery = true, value = "SELECT resource.id, resource.url, resource.contributor, resource.description, resource.subject_id FROM resource INNER JOIN subject ON resource.subject_id = subject.id ORDER BY subject.semester ASC")
    List<Resource> getAllResources();
    @Query("select r from Resource r where r.subject.id = ?1")
    List<Resource> getResourcesBySubjectId(Integer subjectId);

    @Query(nativeQuery = true, value = "SELECT resource.id, resource.url, resource.contributor, resource.description, resource.subject_id FROM resource INNER JOIN subject ON resource.subject_id = subject.id WHERE subject.semester = ?1 ORDER BY subject.name ASC ")
    List<Resource> getResourcesBySubjectSemester(Integer semester);

    @Query(nativeQuery = true, value = "SELECT * FROM resource WHERE LOWER(contributor) LIKE LOWER(?1)")
    List<Resource> searchResourcesByContributor(String contributor);
}
