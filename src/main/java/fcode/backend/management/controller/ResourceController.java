package fcode.backend.management.controller;

import fcode.backend.management.model.dto.ResourceDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceController {
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

    @GetMapping("/{resourceId}")
    public Response<ResourceDTO> getOneResources(@PathVariable Integer resourceId) {
        return resourceService.getResourceById(resourceId);
    }

    @PostMapping
    public Response<Void> createResource(@RequestBody ResourceDTO resourceDTO) {
        return resourceService.createResource(resourceDTO);
    }

    @PutMapping
    public Response<Void> updateResource(@RequestBody ResourceDTO resourceDTO) {
        return resourceService.updateResource(resourceDTO);
    }

    @DeleteMapping(value = "/{resourceId}")
    public Response<Void> deleteResource(@PathVariable int resourceId) {
        return resourceService.deleteResource(resourceId);
    }

}
