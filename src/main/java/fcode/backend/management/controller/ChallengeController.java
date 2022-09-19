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

    @GetMapping(value = "/all")
    public Response<List<ChallengeDTO>> getAllChallenges() {
        return challengeServices.getAllChallenge();
    }

    @GetMapping(value = "/id/{id}")
    public Response<ChallengeDTO> getChallengeById(@PathVariable Integer id) {
        return challengeServices.getChallengeById(id);
    }

    @GetMapping(value = "/title/{title}")
    public Response<ChallengeDTO> getChallengeByTitle(@PathVariable String title) {
        return challengeServices.getChallengeByTitle(title);
    }

    @PostMapping(value = "/new")
    public Response<Void> createChallenge(@RequestBody ChallengeDTO challengeDTO) {
        return challengeServices.createChallenge(challengeDTO);
    }

    @PutMapping
    public Response<Void> updateChallenge(@RequestBody ChallengeDTO challengeDTO) {
        return challengeServices.updateChallenge(challengeDTO);
    }

    @DeleteMapping(value = "/{id}")
    public Response<Void> deleteChallenge(@PathVariable Integer id) {
        return challengeServices.deleteChallenge(id);
    }
}
