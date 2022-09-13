package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query(nativeQuery = true, value = "SELECT * from event WHERE status = 'active'")
    List<Event> findAllEvent();

    @Query(nativeQuery = true, value = "SELECT * from event WHERE id = ?1 AND status = 'active'")
    Event findEventById(Integer id);

    @Query(nativeQuery = true, value = "SELECT * from event WHERE name  like ?1 AND status = 'active'")
    List<Event> findEventsByName(String name);

    @Query
    Event findByName(String name);
}

