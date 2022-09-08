package fcode.backend.management;

import fcode.backend.management.model.dto.ChallengeDTO;
import fcode.backend.management.service.ChallengeServices;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ChallengeServiceTest {

    @Autowired
    ChallengeServices challengeServices;


//        @Test
//    void getChallenge() {
//        List<ChallengeDTO> challengeDTOList = challengeServices.getAllChallenge().getData();
//        challengeDTOList.forEach(System.out::println);
//    }
//
//    @Test
//     void createChallenge() {
//        challengeServices.createChallenge(new ChallengeDTO("Thử thách 1", "Test 1", "http://"));
//        challengeServices.createChallenge(new ChallengeDTO("Thử thách 2", "Test 2", "https://"));
//        challengeServices.createChallenge(new ChallengeDTO("Thử thách 3", "Test 3", "httpss://"));
//    }
//
//    @Test
//    void updateChallenge() {
//        challengeServices.updateChallenge(new ChallengeDTO(1), "Thử thách một", null, null);
//        challengeServices.updateChallenge(new ChallengeDTO(2), "Thử thách hai", null, null);
//        challengeServices.updateChallenge(new ChallengeDTO(3), "Thử thách ba", null, null);
//    }
//
//    @Test
//    void deleteChallenge() {
//        challengeServices.deleteChallenge(1);
//    }
//
//    @Test
//    void getChallengeById() {
//        ChallengeDTO challengeDTO1 = challengeServices.getChallengeById(1).getData();
//        System.out.println(challengeDTO1);
//        ChallengeDTO challengeDTO2 = challengeServices.getChallengeById(2).getData();
//        System.out.println(challengeDTO2);
//        ChallengeDTO challengeDTO3 = challengeServices.getChallengeById(3).getData();
//        System.out.println(challengeDTO3);
//    }
//
//    @Test
//    void getChallengeByTitle() {
//         ChallengeDTO challengeDTO1 = challengeServices.getChallengeByTitle("Thử thách một").getData();
//         System.out.println(challengeDTO1);
//         ChallengeDTO challengeDTO2 = challengeServices.getChallengeByTitle("Thử thách 2").getData();
//         System.out.println(challengeDTO2);
//         ChallengeDTO challengeDTO3 = challengeServices.getChallengeByTitle("Thử thách ba").getData();
//         System.out.println(challengeDTO3);
//    }

}
