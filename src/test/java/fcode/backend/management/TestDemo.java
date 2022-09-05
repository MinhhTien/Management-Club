package fcode.backend.management;

import fcode.backend.management.model.dto.SubjectDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import fcode.backend.management.service.SubjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestDemo {
    @Autowired
    SubjectService subjectService;

    @Test
    public void addSubject1() {
        subjectService.createSubject(new SubjectDTO("CSI101", 1));
        subjectService.createSubject(new SubjectDTO("SSG104", 2));
        subjectService.createSubject(new SubjectDTO("CSD104", 3));
    }

    @Test
    public void getSubjects() {
        System.out.println(subjectService.getAllSubjects().getData());
    }

    @Test
    public void getSubjects1() {
        System.out.println(subjectService.searchSubjects("c").getData());
    }

    @Test
    public void getOneSubject() {
        System.out.println(subjectService.getSubjectById(8).getData());
    }

    @Test
    public void getSubjectByName() {
        System.out.println(subjectService.getSubjectByName("CEA101").getData());
    }

    @Test
    public void getSubjectsBySemester() {
        System.out.println(subjectService.getSubjectsBySemester(3).getData());
    }

    @Test
    public void updateSubject1() {
        subjectService.updateSubject(new SubjectDTO(1,"JPD103",3));
    }

    @Test
    public void updateSubject2() {
        subjectService.updateSubject(new SubjectDTO(3,"CSI101",3));
    }

    @Test
    public void deleteSubject1() {
        subjectService.deleteSubject(3);
    }

    @Test
    public void deleteSubject2() {
        subjectService.deleteSubject(10);
    }
}
