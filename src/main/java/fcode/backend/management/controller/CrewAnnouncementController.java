package fcode.backend.management.controller;

import fcode.backend.management.model.dto.CrewAnnouncementDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.CrewAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/crewAnnouncement")
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
    @GetMapping("/crew/{crewId}")
    public Response<Set<CrewAnnouncementDTO>> getAnnouncementOfACrew(@PathVariable Integer crewId) {
        return crewAnnouncementService.getAnnouncementsOfACrew(crewId);
    }
    @GetMapping("/all")
    public Response<Set<CrewAnnouncementDTO>> getAllCrewAnnouncement() {
        return crewAnnouncementService.getAllCrewAnnouncements();
    }

    @GetMapping("/processing")
    public Response<Set<CrewAnnouncementDTO>> getProcessingCrewAnnouncement() {
        return crewAnnouncementService.getProcessingCrewAnnouncements();
    }
    @GetMapping("/inactive")
    public Response<Set<CrewAnnouncementDTO>> getInactiveCrewAnnouncement() {
        return crewAnnouncementService.getInactiveCrewAnnouncements();
    }

    @PutMapping("/approve/{crewAnnouncementId}")
    public Response<Void> approveAnnouncement(@PathVariable Integer crewAnnouncementId) {
        return crewAnnouncementService.approveCrewAnnouncement(crewAnnouncementId);
    }

    @PutMapping("/disapprove/{crewAnnouncementId}")
    public Response<Void> disapproveAnnouncement(@PathVariable Integer crewAnnouncementId) {
        return crewAnnouncementService.disapproveCrewAnnouncement(crewAnnouncementId);
    }

    @PutMapping("/approve/all")
    public Response<Void> approveAll() {
        return crewAnnouncementService.approveAll();
    }

    @PutMapping("/disapprove/all")
    public Response<Void> disapproveAll() {
        return crewAnnouncementService.disapproveAll();
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
