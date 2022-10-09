package fcode.backend.management;

import fcode.backend.management.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EventServiceTest {
    @Autowired
    EventService eventService;

//    @Test
//    void getAllEvetns(){
//        List<EventDTO> eventDTOList = eventService.getAllEvents().getData();
//        eventDTOList.forEach(System.out::println);
//    }
//
//    @Test
//    void getEventById(){
//        EventDTO event = eventService.getEventById(1).getData();
//        System.out.println(event);
//    }
//
//    @Test
//    void getEventByName(){
//        List<EventDTO> eventDTOList = eventService.getEventsByName("Sự kiện").getData();
//        eventDTOList.forEach(System.out::println);
//    }
//    long time = System.currentTimeMillis();
//    java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
//    @Test
//    void createEvent() {
//
//        eventService.createEvent(new EventDTO("Sự kiện 4", 350, "Sự kiện 4", timestamp, timestamp, "Hall N"));
//    }
//
//    @Test
//    void updateEvent(){
//        eventService.updateEvent(1, "Sự kiện đặc biệt 2", 545, "Sự kiện special", null, null, "HALPP");
//    }
//
//    @Test
//    void deleteEvent(){
//        eventService.deleteEvent(4);
//    }
}
