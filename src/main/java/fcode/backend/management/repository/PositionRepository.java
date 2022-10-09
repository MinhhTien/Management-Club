package fcode.backend.management.repository;

import fcode.backend.management.model.dto.MemberDTO;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.repository.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM position")
    List<Position> getAllPosition();

    @Query
    Position getById(Integer id);
}
