package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Resource;
import fcode.backend.management.repository.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    Resource findOneById(Integer resourceId);
    Resource findResourceByUrl(String url);
    List<Resource> findResourcesBySubject(Integer subjectId);
    @Query(nativeQuery = true, value = "SELECT subject_id FROM resource WHERE id = ?1")
    Integer findSubjectId(Integer resourceId);

    @Query(nativeQuery = true, value = "SELECT * FROM resource")//ORDER BY subject_id.semester???
    Set<Resource> getAllResources();

    boolean existsByUrl(String resourceUrl);
    boolean existsById(Integer id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO resource(url, contributor, description, subject_id) VALUES (?1, ?2, ?3, ?4)")
    void insertResource(String url, String contributor, String description, Integer subjectId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE resource SET url = ?2 WHERE id = ?1")
    void updateResource(Integer id, String newUrl);


    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM resource WHERE id = ?1")
    void deleteById(Integer id);
}
