package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import fcode.backend.management.service.constant.Status;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM announcement WHERE status = ?1 ORDER BY updated_time ASC ")
    List<Announcement> getAllAnnouncements(Status status);

    Announcement getByIdAndStatus(Integer id, Status status);

    @Query(nativeQuery = true, value = "SELECT * FROM announcement WHERE LOWER(title) LIKE LOWER(?1) AND status = ?2")
    List<Announcement> searchAllByTitle(String value, Status status);
}
