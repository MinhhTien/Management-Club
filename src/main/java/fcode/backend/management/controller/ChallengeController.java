package fcode.backend.management.controller;

import fcode.backend.management.model.dto.ChallengeDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.ChallengeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challenge")
public class ChallengeController {
    @Autowired
    ChallengeServices challengeServices;

    @GetMapping(value = "/allChallenges")
    public Response<List<ChallengeDTO>> getAllChallenges() {
        return challengeServices.getAllChallenge();
    }

    @GetMapping(value = "/{id}")
    public Response<ChallengeDTO> getChallgenById(@PathVariable Integer id) {
        return challengeServices.getChallengeById(id);
    }

    @GetMapping(value = "/{title}")
    public Response<ChallengeDTO> getChallengeByTitle(@PathVariable String title) {
        return challengeServices.getChallengeByTitle(title);
    }

    @PostMapping(value = "/newChallenge")
    public Response<Void> createChallenge(@RequestBody ChallengeDTO challengeDTO) {
        return challengeServices.createChallenge(challengeDTO);
    }

    @PutMapping
    public Response<Void> updateChallenge(@RequestBody ChallengeDTO challengeDTO, String title, String description, String registerUrl) {
        return challengeServices.updateChallenge(challengeDTO, title, description, registerUrl);
    }

    @DeleteMapping(value = "/{id}")
    public Response<Void> deleteChallenge(@PathVariable Integer id) {
        return challengeServices.deleteChallenge(id);
    }
}
