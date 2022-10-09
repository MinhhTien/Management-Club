package fcode.backend.management.service;

import fcode.backend.management.model.dto.ResourceDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.ResourceRepository;
import fcode.backend.management.repository.SubjectRepository;
import fcode.backend.management.repository.entity.Resource;
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
public class ResourceService {
    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(ResourceService.class);
    private static final String CREATE_RESOURCE = "Create resource: ";
    private static final String UPDATE_RESOURCE = "Update resource: ";
    private static final String DELETE_RESOURCE = "Delete resource: ";
    private static final String SUBJECT_NOT_EXIST = "Subject not exist.";

    public Response<List<ResourceDTO>> getAllResources() {
        logger.info("getResources()");

        List<ResourceDTO> resourceDTOList = resourceRepository.getAllResources().stream()
                .map(resourceEntity -> {
                    ResourceDTO resourceDTO = modelMapper.map(resourceEntity, ResourceDTO.class);
                    resourceDTO.setSubjectId(resourceEntity.getSubject().getId());
                    return resourceDTO;
                }).collect(Collectors.toList());

        logger.info("Get all resources success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), resourceDTOList);
    }

    public Response<List<ResourceDTO>> getResourcesBySubjectId(Integer subjectId) {
        logger.info("getResourcesBySubjectId(subjectId: {})", subjectId);

        Subject subject = subjectRepository.findSubjectById(subjectId);
        if(subject == null) {
            logger.warn("{}{}", "Get resources by subject id:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        List<ResourceDTO> resourceDTOList = subject.getResourceList()
                .stream().map(resourceEntity -> modelMapper.map(resourceEntity, ResourceDTO.class)).collect(Collectors.toList());

        if(resourceDTOList.isEmpty()) {
            logger.warn("{}{}", "Get resources by subject id:", "Subject has no resource");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Subject has no resource");
        }
        logger.info("Get resources by subjectId success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), resourceDTOList);
    }

    public Response<List<ResourceDTO>> getResourcesBySemester(Integer semester) {
        logger.info("getResourcesBySemester(semester: {})", semester);

        List<ResourceDTO> resourceDTOList = resourceRepository.getResourcesBySubjectSemester(semester).stream()
                .map(resourceEntity -> {
                    ResourceDTO resourceDTO = modelMapper.map(resourceEntity, ResourceDTO.class);
                    resourceDTO.setSubjectId(resourceEntity.getSubject().getId());
                    return resourceDTO;
                }).collect(Collectors.toList());
        if(resourceDTOList.isEmpty()) {
            logger.warn("{}{}", "Get resources by semester:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        logger.info("Get resources by semester success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), resourceDTOList);
    }

    public Response<List<ResourceDTO>> searchResourcesByContributor(String contributor) {
        logger.info("searchResourcesByContributor(contributor: {})", contributor);

        List<ResourceDTO> resourceDTOList = resourceRepository.searchResourcesByContributor("% %".replace(" ", contributor)).stream()
                .map(resourceEntity -> {
                    ResourceDTO resourceDTO = modelMapper.map(resourceEntity, ResourceDTO.class);
                    resourceDTO.setSubjectId(resourceEntity.getSubject().getId());
                    return resourceDTO;
                }).collect(Collectors.toList());
        if(resourceDTOList.isEmpty()) {
            logger.warn("{}{}", "Search resources by contributor:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        logger.info("Search resources by contributor success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), resourceDTOList);
    }

    public Response<ResourceDTO> getResourceById(Integer id) {
        logger.info("getResourceById(resourceId: {})", id);

        if(!resourceRepository.existsById(id)) {
            logger.warn("{}{}", "Get resource by id:", ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Resource resource = resourceRepository.findOneById(id);
        ResourceDTO resourceDTO = modelMapper.map(resource, ResourceDTO.class);
        resourceDTO.setSubjectId(resource.getSubject().getId());

        logger.info("{}{}", "Get resource by id: ", ServiceMessage.SUCCESS_MESSAGE.getMessage());
        return  new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), resourceDTO);
    }

    @Transactional
    public Response<Void> createResource(ResourceDTO resourceDto) {
        logger.info("createResource(resourceDto: {})", resourceDto);

        if(resourceDto == null) {
            logger.warn("{}{}",CREATE_RESOURCE, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(resourceRepository.existsByUrl(resourceDto.getUrl())) {
            logger.warn("{}{}", CREATE_RESOURCE, "Resource URL already exist.");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Resource URL already exist.");
        }
        if(!subjectRepository.existsById(resourceDto.getSubjectId())) {
            logger.warn("{}{}", CREATE_RESOURCE, SUBJECT_NOT_EXIST);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), SUBJECT_NOT_EXIST);
        }
        resourceDto.setId(null);
        Resource resource = modelMapper.map(resourceDto, Resource.class);
        resource.setSubject(new Subject(resourceDto.getSubjectId()));
        resourceRepository.save(resource);
        logger.info("Create resource success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> updateResource(ResourceDTO resourceDto) {
        logger.info("updateResource(resourceDto:{})", resourceDto);

        if(resourceDto == null) {
            logger.warn("{}{}", UPDATE_RESOURCE, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        if(!resourceRepository.existsById(resourceDto.getId())){
            logger.warn("{}{}", UPDATE_RESOURCE, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        if(!resourceRepository.existsBySubject(resourceDto.getSubjectId())) {
            logger.warn("{}{}", UPDATE_RESOURCE, SUBJECT_NOT_EXIST);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), SUBJECT_NOT_EXIST);
        }

        Resource resource = modelMapper.map(resourceDto, Resource.class);
        resource.setSubject(new Subject(resourceDto.getSubjectId()));
        resourceRepository.save(resource);
        logger.info("Update resource success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> deleteResource(Integer id) {
        logger.info("deleteResource(resourceId: {})", id);

        if(!resourceRepository.existsById(id)) {
            logger.warn("{}{}", DELETE_RESOURCE, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        Resource resource = resourceRepository.findOneById(id);
        resourceRepository.delete(resource);
        logger.info("Delete resource success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

}

