package fcode.backend.management;

import fcode.backend.management.model.dto.PlusPointDTO;
import fcode.backend.management.repository.PlusPointRepository;
import fcode.backend.management.service.PlusPointService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class PlusPointServiceTest {
    @Autowired
    PlusPointService plusPointService;

    @Autowired
    PlusPointRepository plusPointRepository;

//    @Test
//    void getAllPlusPoint() {
//        List<PlusPointDTO> plusPointDTOList = plusPointService.getAllPlusPoints().getData();
//        plusPointDTOList.forEach(System.out::println);
//    }
//
//    @Test
//    void getById() {
//        PlusPointDTO plusPointDTO = plusPointService.getPlusPointById(1).getData();
//        System.out.println(plusPointDTO.toString());
//    }
//
//    @Test
//    void getByMemberId() {
//        List<PlusPointDTO> plusPointDTOList = plusPointService.getByMemberId(1).getData();
//        plusPointDTOList.forEach(System.out::println);
//    }
//
//    @Test
//    void getBetweenTime() {
//        List<PlusPointDTO> plusPointDTOList = plusPointService.getByMemberIdBetweenTime(1, new Date(), new Date()).getData();
//        plusPointDTOList.forEach(System.out::println);
//    }
//
//    @Test
//    void createPlusPoint() {
//        plusPointService.createPlusPoint(new PlusPointDTO(300, "tham gia sự kiện 2", 2));
//        plusPointService.createPlusPoint(new PlusPointDTO(100, "tham gia sự kiện", 1));
//        plusPointService.createPlusPoint(new PlusPointDTO(240, "tham gia sự kiện", 1));
//        plusPointService.createPlusPoint(new PlusPointDTO(100, "tham gia sự kiện 3", 3));
//    }
//
//    @Test
//    void updatePlusPoint() {
//        plusPointService.updatePlusPoint(new PlusPointDTO(2, 360, "tham gia sự kiện 1", 2));
//    }

//    @Test
//    void deletePlusPoint() {
//        plusPointService.deletePlusPoint(1);
//    }
}
