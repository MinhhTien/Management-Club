package fcode.backend.management.service;

import fcode.backend.management.config.interceptor.Status;
import fcode.backend.management.model.dto.EventDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.EventRepository;
import fcode.backend.management.repository.entity.Event;
import fcode.backend.management.service.constant.ServiceMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(EventService.class);
    private static final String GET_EVENT_BY_ID = "Get event by id: ";
    private static final String CREATE_EVENT = "Create event: ";
    private static final String UPDATE_EVENT = "Update event: ";
    private static final String DELETE_EVENT = "Delete event: ";

    public Response<List<EventDTO>> getAllEvents() {
        logger.info("Get all event");
        List<EventDTO> eventDTOList = eventRepository.findAllEvent(Status.ACTIVE.toString()).stream()
                .map(event -> modelMapper.map(event, EventDTO.class)).collect(Collectors.toList());
        logger.info("Get all event successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), eventDTOList);
    }

    public Response<EventDTO> getEventById(Integer id) {
        logger.info("Get event by ID: {}", id);
        if(id == null) {
            logger.warn("{}{}", GET_EVENT_BY_ID, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(eventRepository.findEventById(id, Status.ACTIVE.toString()) == null) {
            logger.warn("{}{}", GET_EVENT_BY_ID, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Event event = eventRepository.findEventById(id, Status.ACTIVE.toString());
        EventDTO eventDTO = modelMapper.map(event, EventDTO.class);
        logger.info("{}{}", GET_EVENT_BY_ID, ServiceMessage.SUCCESS_MESSAGE);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), eventDTO);
    }

    public Response<List<EventDTO>> getEventsByName(String name) {
        logger.info("Get event by name: {}", name);
        if(name == null) {
            logger.warn("{}{}", "Get events by name: ", ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(eventRepository.findEventsByName("%"+name+"%", Status.ACTIVE.toString()).isEmpty()) {
            logger.warn("{}{}", "Get events by name: ", ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        List<EventDTO> eventDTOList = eventRepository.findEventsByName("%"+name+"%", Status.ACTIVE.toString()).stream()
                .map(event -> modelMapper.map(event, EventDTO.class)).collect(Collectors.toList());
        logger.info("Get events successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), eventDTOList);
    }

    public Response<Void> createEvent(EventDTO eventDTO) {
        logger.info("{}{}", CREATE_EVENT, eventDTO);
        if(eventDTO == null) {
            logger.warn("{}{}", CREATE_EVENT, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(eventRepository.findByName(eventDTO.getName()) != null) {
            logger.warn("{}{}", CREATE_EVENT, "Event is already exist");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Event is already exist");
        }
        Event event = modelMapper.map(eventDTO, Event.class);
        event.setStatus(Status.ACTIVE);
        logger.info("{}{}", CREATE_EVENT, event);
        eventRepository.save(event);
        logger.info("Create event successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateEvent(EventDTO eventDTO) {
        logger.info("{}", UPDATE_EVENT);
        if(eventDTO == null) {
            logger.warn("{}{}", UPDATE_EVENT, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Event eventEntity = eventRepository.findEventById(eventDTO.getId(), Status.ACTIVE.toString());
        if(eventEntity == null) {
            logger.warn("{}{}", UPDATE_EVENT, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if(eventDTO.getName() != null) {
            eventEntity.setName(eventDTO.getName());
        }
        if(eventDTO.getPoint() != null) {
            eventEntity.setPoint(eventDTO.getPoint());
        }
        if(eventDTO.getDescription() != null) {
            eventEntity.setDescription(eventDTO.getDescription());
        }
        if(eventDTO.getStartTime() != null) {
            eventEntity.setStartTime(eventDTO.getStartTime());
        }
        if(eventDTO.getEndTime() != null) {
            eventEntity.setEndTime(eventDTO.getEndTime());
        }
        if(eventDTO.getLocation() != null) {
            eventEntity.setLocation(eventDTO.getLocation());
        }

        eventRepository.save(eventEntity);
        logger.info("Update event successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteEvent(Integer id) {
        logger.info("{}{}", DELETE_EVENT, id);
        if(id == null) {
            logger.warn("{}{}", DELETE_EVENT, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Event eventEntity = eventRepository.findEventById(id, Status.ACTIVE.toString());
        if(eventEntity == null) {
            logger.warn("{}{}", DELETE_EVENT, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        eventEntity.setStatus(Status.INACTIVE);
        eventRepository.save(eventEntity);
        logger.info("Delete event successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
