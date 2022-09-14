package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    boolean existsById(Integer id);
    boolean existsByTitle(String title);

    @Query(nativeQuery = true, value = "SELECT * FROM Announcement WHERE status = 'ACTIVE' ORDER BY created_time ASC ")
    List<Notification> getAllAnnouncements(String status);

    Notification getById(Integer id);

    @Query(nativeQuery = true, value = "SELECT * FROM announcement WHERE LOWER(title) LIKE LOWER(?1)")
    List<Notification> searchAllByTitle(String value);
}
