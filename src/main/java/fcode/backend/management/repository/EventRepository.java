package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Event;
import fcode.backend.management.service.constant.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query(nativeQuery = true, value = "SELECT * from event WHERE status = ?1")
    List<Event> findAllEvent(String status);

    @Query(nativeQuery = true, value = "SELECT * from event WHERE id = ?1 AND status = ?2")
    Event findEventById(Integer id, String status);

    @Query(nativeQuery = true, value = "SELECT * from event WHERE name = ?1 AND status = ?2")
    List<Event> findEventsByName(String name, String status);

    Boolean existsByNameAndAndStatus(String name, Status status);
}

