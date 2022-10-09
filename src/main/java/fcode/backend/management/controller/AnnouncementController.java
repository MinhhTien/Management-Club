package fcode.backend.management.controller;

import fcode.backend.management.model.dto.AnnouncementDTO;
import fcode.backend.management.model.request.CreateAnnouncementRequest;
import fcode.backend.management.model.dto.NotificationDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.AnnouncementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.validator.UrlValidator;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
    private static final String INVALID_IMAGE_URL = "Invalid image url.";
    private static final Logger logger = LogManager.getLogger(AnnouncementController.class);

    @Autowired
    AnnouncementService announcementService;

    @Autowired
    UrlValidator urlValidator;

    @GetMapping("/all")
    public Response<List<AnnouncementDTO>> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    @GetMapping("/one/{announcementId}")
    public Response<AnnouncementDTO> getOneAnnouncement(@PathVariable Integer announcementId) {
        return announcementService.getAnnouncementById(announcementId);
    }

    @GetMapping("/notifications")
    public Response<Set<NotificationDTO>> getNotificationsByMember(@RequestAttribute(required = false) Integer userId) {
        return announcementService.getNotificationsByMember(userId);
    }

    @GetMapping("/search")
    public Response<List<AnnouncementDTO>> searchAnnouncements(@RequestParam String value) {
        return announcementService.searchAnnouncements(value);
    }

    @PostMapping
    public Response<Void> createAnnouncement(@RequestBody CreateAnnouncementRequest createAnnouncementRequest, @RequestAttribute(required = false) Integer userId) {
        if (urlValidator.isValid(createAnnouncementRequest.getImageUrl())) {
            return  announcementService.createAnnouncement(createAnnouncementRequest, userId);
        } else {
            logger.warn("INVALID_IMAGE_URL");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_IMAGE_URL);
        }
    }

    @PutMapping
    public Response<Void> updateAnnouncement(@RequestBody AnnouncementDTO announcementDTO, @RequestAttribute(required = false) Integer userId) {
        if (urlValidator.isValid(announcementDTO.getImageUrl())) {
            return announcementService.updateAnnouncement(announcementDTO, userId);
        } else {
            logger.warn("INVALID_IMAGE_URL");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_IMAGE_URL);
        }
    }

    @DeleteMapping("/one/{announcementId}")
    public Response<Void> deleteAnnouncement(@PathVariable Integer announcementId) {
        return announcementService.deleteAnnouncement(announcementId);
    }
}
