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
        resourceService.createResource(new ResourceDTO("https://rtgsdgfdgwteeryrey","QuanTP","resource",9));
        resourceService.createResource(new ResourceDTO("https://rnfgfhdfhfdfdfgfdghfdfdsf","TienVM","resource",11));
        resourceService.createResource(new ResourceDTO("https://aqwgnfmdfgdfhfhhsdghfdfdsf","TienVM","resource",12));
    }
    @Test
    public void getResource1() {
        System.out.println(resourceService.getAllResources().getData());
        System.out.println(resourceService.getResourceById(11).getData());
    }

    @Test
    public void getResource2() {
        System.out.println(resourceService.getResourcesBySubjectId(9).getData());
    }

    @Test
    public void searchResource() {
        System.out.println(resourceService.searchResourcesByContributor("Nt").getData());
    }

    @Test
    public void getResource3() {
        System.out.println(resourceService.getResourcesBySemester(1).getData());
    }

    @Test
    public void updateResource1() {
        resourceService.updateResource( new ResourceDTO(9,"http://dbfgsdf","HaiUH","resourceCEA101",12));
    }

    @Test
    public void deleteResource1() {
        resourceService.deleteResource(8);
    }
}
