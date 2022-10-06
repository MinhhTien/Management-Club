package fcode.backend.management;

import fcode.backend.management.repository.PlusPointRepository;
import fcode.backend.management.service.PlusPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    String sDate1="01/09/2022";
    String sDate2="30/09/2022";
    Date date1;
    Date date2;

    {
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
            date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

//    @Test
//    void getBetweenTime() {
//        List<PlusPointDTO> plusPointDTOList = plusPointService.getByMemberIdBetweenTime(2, date1, date2).getData();
//        plusPointDTOList.forEach(System.out::println);
//    }
//    @Test
//    void getTotalPoint() {
//        PlusPointDTO plusPointDTO = plusPointService.getTotalPointInPeriodTime(3, date1, date2).getData();
//        System.out.println(plusPointDTO);
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
