package fcode.backend.management.controller;

import fcode.backend.management.model.dto.AnnouncementDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.validator.UrlValidator;

import java.util.List;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
    private static final String INVALID_IMAGE_URL = "Invalid image url.";

    @Autowired
    AnnouncementService announcementService;

    @Autowired
    UrlValidator urlValidator;

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
    public Response<Void> createAnnouncement(@RequestBody AnnouncementDTO announcementDTO, @RequestAttribute(required = false) Integer userId) {
        if (urlValidator.isValid(announcementDTO.getImageUrl())) {
            return  announcementService.createAnnouncement(announcementDTO, userId);
        } else {
            return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_IMAGE_URL);
        }
    }

    @PutMapping
    public Response<Void> updateAnnouncement(@RequestBody AnnouncementDTO announcementDTO, @RequestAttribute(required = false) Integer userId) {
        if (urlValidator.isValid(announcementDTO.getImageUrl())) {
            return announcementService.updateAnnouncement(announcementDTO, userId);
        } else {
            return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_IMAGE_URL);
        }
    }

    @DeleteMapping("/{announcementId}")
    public Response<Void> deleteAnnouncement(@PathVariable Integer announcementId) {
        return announcementService.deleteAnnouncement(announcementId);
    }
}
