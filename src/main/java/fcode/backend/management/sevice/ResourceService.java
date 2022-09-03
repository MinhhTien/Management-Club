package fcode.backend.management.sevice;

import fcode.backend.management.model.dto.ResourceDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.ResourceRepository;
import fcode.backend.management.repository.SubjectRepository;
import fcode.backend.management.repository.entity.Resource;
import fcode.backend.management.sevice.constant.ServiceMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
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

    public Response<Set<ResourceDTO>> getAllResources() {
        logger.info("getResources()");

        Set<ResourceDTO> resourceDTOSet = resourceRepository.getAllResources().stream()
                .map(resourceEntity -> modelMapper.map(resourceEntity, ResourceDTO.class)).collect(Collectors.toSet());

        logger.info("Get all resources success");
        return new Response<>(200, ServiceMessage.SUCCESS_MESSAGE.getMessage(), resourceDTOSet);
    }

    public Response<Set<ResourceDTO>> getResourcesBySubjectId(Integer subjectId) {
        logger.info("getResourcesBySubjectId(subjectId: {})", subjectId);

        Set<ResourceDTO> resourceDTOSet = resourceRepository.getResourcesBySubjectId(subjectId).stream()
                .map(resourceEntity -> modelMapper.map(resourceEntity, ResourceDTO.class)).collect(Collectors.toSet());
        if(resourceDTOSet.size() == 0) {
            logger.warn("{}{}", "Get resources by subject id:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(400, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        logger.info("Get resources by subjectId success");
        return new Response<>(200, ServiceMessage.SUCCESS_MESSAGE.getMessage(), resourceDTOSet);
    }

    public Response<Set<ResourceDTO>> getResourcesBySemester(Integer semester) {
        logger.info("getResourcesBySemester(semester: {})", semester);

        Set<ResourceDTO> resourceDTOSet = resourceRepository.getResourcesBySubjectSemester(semester).stream()
                .map(resourceEntity -> modelMapper.map(resourceEntity, ResourceDTO.class)).collect(Collectors.toSet());
        if(resourceDTOSet.size() == 0) {
            logger.warn("{}{}", "Get resources by semester:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(400, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        logger.info("Get resources by semester success");
        return new Response<>(200, ServiceMessage.SUCCESS_MESSAGE.getMessage(), resourceDTOSet);
    }

    public Response<ResourceDTO> getResourceById(Integer id) {
        logger.info("getResourceById(resourceId: {})", id);

        ResourceDTO resourceDTO = modelMapper.map(resourceRepository.findOneById(id), ResourceDTO.class);
        if(resourceDTO == null) {
            logger.warn("{}{}", "Get resource by id:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(400, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        logger.info("{}{}", "Get resource by id: ", ServiceMessage.SUCCESS_MESSAGE.getMessage());
        return  new Response<>(200, ServiceMessage.SUCCESS_MESSAGE.getMessage(), resourceDTO);
    }

    @Transactional
    public Response<Void> createResource(ResourceDTO resourceDto) {
        logger.info("createResource(resourceDto: {})", resourceDto);

        if(resourceDto == null) {
            logger.warn("{}{}",CREATE_RESOURCE, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<Void>(400, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(resourceRepository.findResourceByUrl(resourceDto.getUrl()) != null) {
            logger.warn("{}{}", CREATE_RESOURCE, "Resource URL already exist.");
            return new Response<>(400, "Resource URL already exist.");
        }
        Resource resource = modelMapper.map(resourceDto, Resource.class);
        resourceRepository.save(resource);
        logger.info("Create resource success");
        return new Response<Void>(200, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> updateResource(Integer resourceId,ResourceDTO resourceDto) {
        logger.info("updateResource(resourceId:{})", resourceId);

        if(resourceDto == null) {
            logger.warn("{}{}", UPDATE_RESOURCE, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(400, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        if(!resourceRepository.existsById(resourceId)){
            logger.warn("{}{}", UPDATE_RESOURCE, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(400, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        if(resourceRepository.findResourceByUrl(resourceDto.getUrl()) != null) {
            logger.warn("{}{}", UPDATE_RESOURCE, "Resource URL already exist");
            return new Response<>(400, "Resource URL already exist");
        }

        Resource resource = modelMapper.map(resourceDto, Resource.class);
        resource.setId(resourceId);
        resourceRepository.save(resource);
        logger.info("Update resource success");
        return new Response<>(200, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> deleteResource(Integer id) {
        logger.info("deleteResource(resourceId: {})", id);

        if(!resourceRepository.existsById(id)) {
            logger.warn("{}{}", DELETE_RESOURCE, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(400, ServiceMessage.ID_NOT_EXIST_MESSAGE.toString());
        }

        Resource resource = resourceRepository.findOneById(id);
        resourceRepository.delete(resource);
        logger.info("Delete resource success");
        return new Response<>(200, ServiceMessage.SUCCESS_MESSAGE.toString());
    }

}

