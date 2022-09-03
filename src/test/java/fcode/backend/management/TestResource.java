package fcode.backend.management;

import fcode.backend.management.model.dto.ResourceDTO;
import fcode.backend.management.service.ResourceService;
import fcode.backend.management.service.SubjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestResource {
    @Autowired
    ResourceService resourceService;

    @Autowired
    SubjectService subjectService;

    @Test
    public void addResource1() {
        resourceService.createResource(new ResourceDTO("https://wqrwteeryrey","QuanTP","resourceSSG",4));
        resourceService.createResource(new ResourceDTO("https://aqwfsjdsfhhsdghfdfdsf","TienVM","resourceCSI",1));
    }
    @Test
    public void getResource1() {
        System.out.println(resourceService.getAllResources().getData());
        System.out.println(resourceService.getResourceById(2).getData());
    }

    @Test
    public void getResource2() {
        System.out.println(resourceService.getResourcesBySubjectId(1).getData());
    }

    @Test
    public void searchResource() {
        System.out.println(resourceService.searchResourcesByContributor("Nt").getData());
    }

    @Test
    public void getResource3() {
        System.out.println(resourceService.getResourcesBySemester(2).getData());
    }

    @Test
    public void updateResource1() {
        resourceService.updateResource(1, new ResourceDTO("http://dbfgsdf","HaiUH","resourceCEA101",1));
    }

    @Test
    public void deleteResource1() {
        resourceService.deleteResource(2);
    }
}
