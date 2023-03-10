package fcode.backend.management.controller;

import fcode.backend.management.model.dto.ResourceDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    private static final String INVALID_RESOURCE_URL = "Invalid resource url.";
    private static final String URL_PREFIX = "https://";

    @Autowired
    ResourceService resourceService;

    @GetMapping("/all")
    public Response<List<ResourceDTO>> getAllResources() {
        return resourceService.getAllResources();
    }

    @GetMapping("/semester/{semester}")
    public Response<List<ResourceDTO>> getResourcesBySemester(@PathVariable Integer semester) {
        return resourceService.getResourcesBySemester(semester);
    }

    @GetMapping("/subject/{subjectId}")
    public Response<List<ResourceDTO>> getResourcesBySubject(@PathVariable Integer subjectId) {
        return resourceService.getResourcesBySubjectId(subjectId);
    }

    @GetMapping("/contributor")
    public Response<List<ResourceDTO>> searchResourcesByContributor(@RequestParam String contributor) {
        return resourceService.searchResourcesByContributor(contributor);
    }

    @GetMapping("/one/{resourceId}")
    public Response<ResourceDTO> getOneResource(@PathVariable Integer resourceId) {
        return resourceService.getResourceById(resourceId);
    }

    @PostMapping
    public Response<Void> createResource(@RequestBody ResourceDTO resourceDTO) {
        if (resourceDTO.getUrl().startsWith(URL_PREFIX)) {
            return resourceService.createResource(resourceDTO);
        } else {
            return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_RESOURCE_URL);
        }
    }

    @PutMapping
    public Response<Void> updateResource(@RequestBody ResourceDTO resourceDTO) {
        if (resourceDTO.getUrl().startsWith(URL_PREFIX)) {
            return resourceService.updateResource(resourceDTO);
        } else {
            return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_RESOURCE_URL);
        }
    }

    @DeleteMapping(value = "/one/{resourceId}")
    public Response<Void> deleteResource(@PathVariable int resourceId) {
        return resourceService.deleteResource(resourceId);
    }

}
