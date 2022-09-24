package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Integer> {
    Crew findCrewById(Integer id);
}
