package fcode.backend.management.controller;

import fcode.backend.management.model.dto.SubjectDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    SubjectService subjectService;

    @GetMapping("/all")
    public Response<List<SubjectDTO>> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/semester/{semester}")
    public Response<List<SubjectDTO>> getSubjectsBySemester(@PathVariable Integer semester) {
        return subjectService.getSubjectsBySemester(semester);
    }

    @GetMapping("/one/{subjectId}")
    public Response<SubjectDTO> getOneSubject(@PathVariable Integer subjectId) {
        return subjectService.getSubjectById(subjectId);
    }

    @GetMapping("/name/{name}")
    public Response<SubjectDTO> getSubjectsByName(@PathVariable String name) {
        return subjectService.getSubjectByName(name);
    }

    @GetMapping("/search")
    public Response<List<SubjectDTO>> searchSubjects(@RequestParam String value) {
        return subjectService.searchSubjects(value);
    }

    @PostMapping
    public Response<Void> createSubject(@RequestBody SubjectDTO subjectDTO) {
        return  subjectService.createSubject(subjectDTO);
    }

    @PutMapping
    public Response<Void> updateSubject(@RequestBody SubjectDTO subjectDTO) {
        return subjectService.updateSubject(subjectDTO);
    }

    @DeleteMapping("/one/{subjectId}")
    public Response<Void> deleteSubject(@PathVariable Integer subjectId) {
        return subjectService.deleteSubject(subjectId);
    }

}
