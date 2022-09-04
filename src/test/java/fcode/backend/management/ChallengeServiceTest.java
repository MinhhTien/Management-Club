package fcode.backend.management;

import fcode.backend.management.model.dto.ChallengeDTO;
import fcode.backend.management.service.ChallengeServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ChallengeServiceTest {

    @Autowired
    ChallengeServices challengeServices;

//        @Test
//    void getChallenge() {
//        List<ChallengeDTO>challengeDTOList = challengeServices.getAllChallenge().getData();
//        challengeDTOList.forEach(System.out::println);
//    }

//    @Test
//    void createChallenge() {
//        challengeServices.createChallenge(new ChallengeDTO("Thử thách 1", "Test 1", "http://"));
//        challengeServices.createChallenge(new ChallengeDTO("Thử thách 2", "Test 2", "https://"));
//        challengeServices.createChallenge(new ChallengeDTO("Thử thách 3", "Test 3", "httpss://"));
//    }

//    @Test
//    void updateChallenge() {
//        challengeServices.updateChallenge(new ChallengeDTO(3), "Thử thách ba", "Test2", null);
//    }

//    @Test
//    void deleteChallenge() {
//        challengeServices.deleteChallenge(1);
//    }

//    @Test
//    void restoreChallenge() {
//        challengeServices.restoreChallenge(1);
//    }
}
