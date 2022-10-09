package fcode.backend.management;

import fcode.backend.management.model.dto.AnnouncementDTO;
import fcode.backend.management.service.AnnouncementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestAnnouncement {
    @Autowired
    AnnouncementService announcementService;

    @Test
    public void addAnnouncements() {
        //announcementService.createAnnouncement(new AnnouncementDTO("Hop CLB 121","safafdf","sadsad","asdasd","sdfsdf","https://github.com/F-Code-BE/fcode-management/tree/dev","sdfdsf","sdfdsf"), 1);
        //announcementService.createAnnouncement(new AnnouncementDTO("Hop CLB 2",null,"safdsfsad","adsfasd","dsfsdsdf","sdfsdg","werwedsf","swerwedsf"), 1);
    }

    @Test
    public void addAnnouncement1() {
        //announcementService.createAnnouncement(new AnnouncementDTO("Hop CLB 3","safafdf","sadsad","asdasd"),1);
    }

    @Test
    public void getAllAnnouncements() {
        System.out.println(announcementService.getAllAnnouncements().getData());
    }

    @Test
    public void getOneAnnouncement1() {
        System.out.println(announcementService.getAnnouncementById(1).getData());
    }
    @Test
    public void updateAnnouncement2() {
        announcementService.updateAnnouncement(new AnnouncementDTO(3,null, "", null, "String infoUserId", "String location", "String imageUrl", true, "String mail", "String mailTitle"), 1);
    }

    @Test
    public void deleteAnnouncement2() {
        announcementService.deleteAnnouncement(1);
    }

    @Test
    public void searchAnnouncement2() {
        System.out.println(announcementService.searchAnnouncements("hO").getData());
    }
}
