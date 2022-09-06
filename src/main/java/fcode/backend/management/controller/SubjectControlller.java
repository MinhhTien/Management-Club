package fcode.backend.management.controller;

import fcode.backend.management.model.dto.SubjectDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectControlller {
    @Autowired
    SubjectService subjectService;

    @GetMapping("/all")
    public Response<List<SubjectDTO>> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/hasSemester")
    public Response<List<SubjectDTO>> getSubjectsBySemester(@RequestParam Integer semester) {
        return subjectService.getSubjectsBySemester(semester);
    }

    @GetMapping("/{subjectId}")
    public Response<SubjectDTO> getOneSubject(@PathVariable Integer subjectId) {
        return subjectService.getSubjectById(subjectId);
    }

    @GetMapping("/hasName")
    public Response<SubjectDTO> getSubjectsByName(@RequestParam String name) {
        return subjectService.getSubjectByName(name);
    }

    @GetMapping("/search")
    public Response<List<SubjectDTO>> searchSubjects(@RequestParam String value) {
        return subjectService.searchSubjects(value);
    }

    @PostMapping
    public Response<Void> createSubject(@RequestBody SubjectDTO subjectDTO,
                                        @RequestAttribute(required = false) String userId) {
        return  subjectService.createSubject(subjectDTO);//userId
    }

    @PutMapping
    public Response<Void> updateSubject(@RequestBody SubjectDTO subjectDTO,
                                        @RequestAttribute(required = false) String userId) {
        return subjectService.updateSubject(subjectDTO);//userId
    }

    @DeleteMapping("/{subjectId}")
    public Response<Void> deleteSubject(@PathVariable Integer subjectId,
                                        @RequestAttribute(required = false) String userId) {
        return subjectService.deleteSubject(subjectId);//userId
    }

}
