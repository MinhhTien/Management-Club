package fcode.backend.management.controller;

import fcode.backend.management.model.dto.AnnouncementDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
    @Autowired
    AnnouncementService announcementService;

    @GetMapping("/all")
    public Response<List<AnnouncementDTO>> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    @GetMapping("/{announcementId}")
    public Response<AnnouncementDTO> getOneAnnouncement(@PathVariable Integer announcementId) {
        return announcementService.getAnnouncementById(announcementId);
    }

    @GetMapping("/search")
    public Response<List<AnnouncementDTO>> searchAnnouncements(@RequestParam String value) {
        return announcementService.searchAnnouncements(value);
    }

    @PostMapping
    public Response<Void> createAnnouncement(@RequestBody AnnouncementDTO announcementDTO, @RequestAttribute Integer userId) {
        return  announcementService.createAnnouncement(announcementDTO, userId);
    }

    @PutMapping
    public Response<Void> updateAnnouncement(@RequestBody AnnouncementDTO announcementDTO) {
        return announcementService.updateAnnouncement(announcementDTO);
    }

    @DeleteMapping("/{announcementId}")
    public Response<Void> deleteAnnouncement(@PathVariable Integer announcementId) {
        return announcementService.deleteAnnouncement(announcementId);
    }
}
