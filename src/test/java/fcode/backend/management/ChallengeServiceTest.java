package fcode.backend.management;

import fcode.backend.management.service.ChallengeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChallengeServiceTest {

    @Autowired
    ChallengeServices challengeServices;

//        @Test
//    void getChallenge() {
//        List<ChallengeDTO>challengeDTOList = challengeServices.getAllChallenge().getData();
//        challengeDTOList.forEach(System.out::println);
//    }
//
//    @Test
//    void createChallenge() {
//        challengeServices.createChallenge(new ChallengeDTO("Thử thách 1", "Test 1", "http://"));
//    }
//
//    @Test
//    void updateChallenge() {
//        challengeServices.updateChallenge(new ChallengeDTO(1), "Thử thách một", "Test 1", null);
//    }
//
//    @Test
//    void deleteChallenge() {
//        challengeServices.deleteChallenge(1);
//    }

}
