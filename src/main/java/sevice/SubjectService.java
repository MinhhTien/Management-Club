package sevice;

import fcode.backend.management.model.dto.SubjectDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.ResourceRepository;
import fcode.backend.management.repository.SubjectRepository;
import fcode.backend.management.repository.entity.Subject;
import sevice.constant.ServiceMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(SubjectService.class);
    private static final String CREATE_SUBJECT = "Create subject: ";
    private static final String UPDATE_SUBJECT = "Update subject: ";
    private static final String DELETE_SUBJECT = "Delete subject: ";

    public Response<Set<SubjectDTO>> getAllSubjects() {
        logger.info("getSubjects()");

        Set<SubjectDTO> subjectDTOSet = subjectRepository.getAllSubjects().stream()
                .map(subjectEntity -> modelMapper.map(subjectEntity, SubjectDTO.class)).collect(Collectors.toSet());

        logger.info("Get all subjects success");
        return new Response<Set<SubjectDTO>>(200, ServiceMessage.SUCCESS_MESSAGE.getMessage(), subjectDTOSet);
    }

    public Response<SubjectDTO> getSubjectById(Integer id) {
        logger.info("getSubjectById(subjectId: {})", id);

        SubjectDTO subjectDTO = modelMapper.map(subjectRepository.findOneById(id), SubjectDTO.class);
        if(subjectDTO == null) {
            logger.warn("{}{}", "Get subject by id:", ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(400, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        logger.info("{}{}", "Get subject by id: ", ServiceMessage.SUCCESS_MESSAGE);
        return  new Response<>(200, ServiceMessage.SUCCESS_MESSAGE.getMessage(), subjectDTO);
    }

    public Response<Void> createSubject(SubjectDTO subjectDto) {
        logger.info("createSubject(subjectDto: {})", subjectDto);

        if(subjectDto == null) {
            logger.warn("{}{}",CREATE_SUBJECT, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<Void>(400, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(subjectRepository.findByName(subjectDto.getName()) != null) {
            logger.warn("{}{}", CREATE_SUBJECT, "Subject already exist.");
            return new Response<>(400, "Subject already exist.");
        }
        Subject subject = modelMapper.map(subjectDto, Subject.class);
        subjectRepository.save(subject);
        logger.info("Create subject success");
        return new Response<Void>(200, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateSubject(SubjectDTO subjectDto) {
        logger.info("updateSubject(subjectDto:{})", subjectDto);

        if(subjectDto == null) {
            logger.warn("{}{}", UPDATE_SUBJECT, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(400, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        if(!subjectRepository.existsById(subjectDto.getId())){
            logger.warn("{}{}", UPDATE_SUBJECT, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(400, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        if(subjectRepository.findByName(subjectDto.getName()) != null) {
            logger.warn("{}{}", UPDATE_SUBJECT, "Subject name already exist");
            return new Response<>(400, "Subject name already exist");
        }

        Subject subject = modelMapper.map(subjectDto, Subject.class);
        subjectRepository.save(subject);
        logger.info("Update subject success");
        return new Response<>(200, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteSubject(Integer id) {
        logger.info("deleteSubject(id: {})", id);

        if(!subjectRepository.existsById(id)) {
            logger.warn("{}{}", DELETE_SUBJECT, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(400, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        if(resourceRepository.findResourcesBySubject(id) != null) {
            logger.warn("{}{}", DELETE_SUBJECT, "Some resources use this subject");
            return new Response<>(400, "Some resources use this subject");
        }
        Subject subject = subjectRepository.findOneById(id);
        subjectRepository.delete(subject);
        logger.info("Delete subject success");
        return new Response<>(200, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
