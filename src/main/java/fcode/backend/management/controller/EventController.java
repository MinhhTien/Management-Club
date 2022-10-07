package fcode.backend.management.controller;

import fcode.backend.management.model.dto.EventDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/event")
public class EventController {
    @Autowired
    EventService eventService;

    @GetMapping(value = "/all")
    public Response<List<EventDTO>> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping(value = "/name/{name}")
    public Response<List<EventDTO>> getByName(@PathVariable String name) {
        return eventService.getEventsByName(name);
    }

    @PostMapping(value = "/new")
    public Response<Void> createEvent(@RequestBody EventDTO eventDTO) {
        return eventService.createEvent(eventDTO);
    }

    @PutMapping
    public Response<Void> updateEvent(@RequestBody EventDTO eventDTO) {
        return eventService.updateEvent(eventDTO);
    }

    @DeleteMapping(value = "/{id}")
    public Response<Void> deleteEvent(@PathVariable Integer id) {
        return eventService.deleteEvent(id);
    }
}
