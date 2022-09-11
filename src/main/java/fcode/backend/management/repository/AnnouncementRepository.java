package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Announcement;
import fcode.backend.management.repository.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import fcode.backend.management.service.constant.Status;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    boolean existsById(Integer id);
    boolean existsByTitle(String title);

    @Query(nativeQuery = true, value = "SELECT * FROM announcement WHERE status = 'ACTIVE' ORDER BY updated_time ASC ")
    List<Announcement> getAllAnnouncements(String status);

    Announcement getById(Integer id);

    @Query(nativeQuery = true, value = "SELECT * FROM announcement WHERE LOWER(title) LIKE LOWER(?1)")
    List<Announcement> searchAllByTitle(String value);
}
