package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<Crew, Integer> {
    Crew findCrewById(Integer id);
}
