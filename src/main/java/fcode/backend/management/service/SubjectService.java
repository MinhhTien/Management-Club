package fcode.backend.management.service;

import fcode.backend.management.model.dto.SubjectDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.ResourceRepository;
import fcode.backend.management.repository.SubjectRepository;
import fcode.backend.management.repository.entity.Subject;
import fcode.backend.management.service.constant.ServiceMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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

    public Response<List<SubjectDTO>> getAllSubjects() {
        logger.info("getSubjects()");

        List<SubjectDTO> subjectDTOList = subjectRepository.getAllSubjects().stream()
                .map(subjectEntity -> modelMapper.map(subjectEntity, SubjectDTO.class)).collect(Collectors.toList());

        logger.info("Get all subjects success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), subjectDTOList);
    }

    public Response<List<SubjectDTO>> getSubjectsBySemester(Integer semester) {
        logger.info("getSubjectsBySemester(semester : {})", semester);

        List<SubjectDTO> subjectDTOList = subjectRepository.findBySemester(semester).stream()
                .map(subjectEntity -> modelMapper.map(subjectEntity, SubjectDTO.class)).collect(Collectors.toList());
        if(subjectDTOList.isEmpty()) {
            logger.warn("{}{}", "Get subject by semester:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        logger.info("Get subjects by semester success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), subjectDTOList);
    }

    public Response<SubjectDTO> getSubjectById(Integer id) {
        logger.info("getSubjectById(subjectId: {})", id);

        if(!subjectRepository.existsById(id)) {
            logger.warn("{}{}", "Get subject by id:", ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        SubjectDTO subjectDTO = modelMapper.map(subjectRepository.findSubjectById(id), SubjectDTO.class);

        logger.info("{}{}", "Get subject by id: ", ServiceMessage.SUCCESS_MESSAGE.getMessage());
        return  new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), subjectDTO);
    }

    public Response<SubjectDTO> getSubjectByName(String subjectName) {
        logger.info("getSubjectByName(subjectName: {})", subjectName);

        Subject subject = subjectRepository.findByName(subjectName);
        if(subject == null) {
            logger.warn("{}{}", "Get subject by name:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        SubjectDTO subjectDTO = modelMapper.map(subject, SubjectDTO.class);

        logger.info("{}{}", "Get subject by name: ", ServiceMessage.SUCCESS_MESSAGE.getMessage());
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), subjectDTO);
    }

    public Response<List<SubjectDTO>> searchSubjects(String value) {
        logger.info("searchSubjects(value : {})", value);

        List<SubjectDTO> subjectDTOList = subjectRepository.searchAllByName("% %".replace(" ", value)).stream()
                .map(subjectEntity -> modelMapper.map(subjectEntity, SubjectDTO.class)).collect(Collectors.toList());
        if(subjectDTOList.isEmpty()) {
            logger.warn("{}{}", "Search subject by name:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        logger.info("Search subject by name success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), subjectDTOList);
    }

    @Transactional
    public Response<Void> createSubject(SubjectDTO subjectDto) {
        logger.info("createSubject(subjectDto: {})", subjectDto);

        if(subjectDto == null) {
            logger.warn("{}{}",CREATE_SUBJECT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(subjectRepository.findByName(subjectDto.getName()) != null) {
            logger.warn("{}{}", CREATE_SUBJECT, "Subject already exist.");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Subject already exist.");
        }
        Subject subject = modelMapper.map(subjectDto, Subject.class);
        subject.setId(null);
        System.out.println(subject.toString());
        subjectRepository.save(subject);
        logger.info("Create subject success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
    @Transactional
    public Response<Void> updateSubject(SubjectDTO subjectDto) {
        logger.info("updateSubject(subjectDto:{})", subjectDto);

        if(subjectDto == null) {
            logger.warn("{}{}", UPDATE_SUBJECT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        if(subjectDto.getId()==null || !subjectRepository.existsById(subjectDto.getId())){
            logger.warn("{}{}", UPDATE_SUBJECT, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        Subject oldSubject = subjectRepository.findByName(subjectDto.getName());
        if(oldSubject != null && oldSubject.getSemester().equals(subjectDto.getSemester())) {
            logger.warn("{}{}", UPDATE_SUBJECT, "Subject name already exist");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Subject name already exist");
        }
        Subject subject = modelMapper.map(subjectDto, Subject.class);
        subjectRepository.save(subject);
        logger.info("Update subject success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
    @Transactional
    public Response<Void> deleteSubject(Integer id) {
        logger.info("deleteSubject(subjectId: {})", id);

        if(!subjectRepository.existsById(id)) {
            logger.warn("{}{}", DELETE_SUBJECT, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        if(resourceRepository.existsBySubject(id)) {
            logger.warn("{}{}", DELETE_SUBJECT, "Some resources use this subject");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Some resources use this subject");
        }
        Subject subject = subjectRepository.findSubjectById(id);
        subjectRepository.delete(subject);
        logger.info("Delete subject success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
