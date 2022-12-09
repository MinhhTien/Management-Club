package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
    @Query(nativeQuery = true, value = "SELECT * from challenge WHERE status = ?1")
    List<Challenge> findAllChallenge(String status);

    @Query(nativeQuery = true, value = "SELECT * from challenge WHERE id = ?1 AND status = ?2")
    Challenge findChallengeById(Integer id, String status);

    @Query(nativeQuery = true, value = "SELECT * from challenge WHERE title = ?1 AND status = ?2")
    Challenge findChallengeByTitle(String title, String status);

}
