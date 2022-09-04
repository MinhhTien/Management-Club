package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
    @Query(nativeQuery = true, value = "SELECT * from challenge WHERE status = 'active'")
    List<Challenge> findAllChallenge();

    @Query(nativeQuery = true, value = "SELECT * from challenge WHERE id = ?1")
    Challenge findChallengeById(Integer id);


    @Query(nativeQuery = true, value = "SELECT * from challenge WHERE title = ?1")
    Challenge findChallengeByTitle(String title);

    @Query(nativeQuery = true, value = "SELECT * from challenge WHERE register_url = ?1")
    Challenge findChallengeByRegisterUrl(String registerUrl);

}
