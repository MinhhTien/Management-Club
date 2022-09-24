package fcode.backend.management.controller;

import fcode.backend.management.model.dto.CrewAnnouncementDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.CrewAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/announcement/crew")
public class CrewAnnouncementController {
    @Autowired
    CrewAnnouncementService crewAnnouncementService;
    @PostMapping
    public Response<Void> createCrewAnnouncement(@RequestBody CrewAnnouncementDTO crewAnnouncementDTO) {
        return crewAnnouncementService.createCrewAnnouncement(crewAnnouncementDTO);
    }

    @GetMapping("/{crewAnnouncementId}")
    public Response<CrewAnnouncementDTO> getCrewAnnouncementById(@PathVariable Integer crewAnnouncementId) {
        return crewAnnouncementService.getCrewAnnouncementById(crewAnnouncementId);
    }
    @GetMapping
    public Response<Set<CrewAnnouncementDTO>> getAnnouncementOfACrew(@RequestParam Integer crewId) {
        return crewAnnouncementService.getAnnouncementsOfACrew(crewId);
    }
    @GetMapping("/all")
    public Response<Set<CrewAnnouncementDTO>> getAllCrewAnnouncement() {
        return crewAnnouncementService.getAllCrewAnnouncements();
    }
    @PutMapping
    public Response<Void> updateCrewAnnouncement(@RequestBody CrewAnnouncementDTO crewAnnouncementDTO) {
        return crewAnnouncementService.updateCrewAnnouncement(crewAnnouncementDTO);
    }

    @DeleteMapping("/{crewAnnouncementId}")
    public Response<Void> deleteCrewAnnouncement(@PathVariable Integer crewAnnouncementId) {
        return crewAnnouncementService.deleteCrewAnnouncementById(crewAnnouncementId);
    }

}
