package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
    @Query(nativeQuery = true, value = "SELECT * from challenge WHERE status = 'available'")
    List<Challenge> findAllChallenge();

    @Query(nativeQuery = true, value = "SELECT * from challenge WHERE id = ?1")
    Challenge findChallengeById(Integer id);

    @Query(nativeQuery = true, value = "SELECT * from challenge WHERE status = 'available' and title LIKE '%?1%'")
    List<Challenge> findChallengeByTitle(String title);
}
