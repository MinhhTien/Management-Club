package fcode.backend.management.controller;

import fcode.backend.management.model.dto.CrewDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.CrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crew")
public class CrewController {
    @Autowired
    CrewService crewService;
    @PostMapping
    public Response<Void> createCrew(@RequestBody CrewDTO crewDTO) {
        return crewService.createCrew(crewDTO);
    }
    @GetMapping(value = "/{crewId}")
    public Response<CrewDTO> getCrewById(@PathVariable Integer crewId) {
        return crewService.getCrew(crewId);
    }
    @PutMapping
    public Response<Void> updateCrew(@RequestBody CrewDTO crewDTO) {
        return crewService.updateCrew(crewDTO);
    }
    @PutMapping("/enroll")
    public Response<Void> enrollMemberToCrew(@RequestParam Integer memberId, @RequestParam Integer crewId) {
        return crewService.assignMemberToCrew(memberId, crewId);
    }
    @DeleteMapping("/{crewId}")
    public Response<Void> deleteCrew(@PathVariable Integer crewId) {
        return crewService.deleteCrew(crewId);
    }
}
