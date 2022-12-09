package fcode.backend.management.service;

import fcode.backend.management.config.interceptor.Regex;
import fcode.backend.management.model.dto.RegisterDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.RegisterRepository;
import fcode.backend.management.repository.entity.Register;
import fcode.backend.management.service.constant.ServiceMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RegisterService {
    @Autowired
    RegisterRepository registerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Value("${student.email.domain}")
    private String studentEmailDomain;

    private static final Logger logger = LogManager.getLogger(RegisterService.class);
    private static final String CREATE_REGISTER = "Create register: ";
    private static final String UPDATE_REGISTER = "Update register: ";
    private static final String DELETE_REGISTER = "Delete register: ";
    private static final String GET_ALL_REGISTER = "Get all registers: ";
    private static final String GET_REGISTER_BY_ID = "Get register by id: ";

    Pattern studentIdPattern = Pattern.compile(Regex.STUDENT_ID_PATTERN);
    Pattern mailPattern = Pattern.compile(Regex.MAIL_PATTERN);

    public Response<List<RegisterDTO>> getAllRegisters() {
        logger.info(GET_ALL_REGISTER);
        List<RegisterDTO> registerDTOList = registerRepository.getAllRegisters().stream()
                .map(register -> modelMapper.map(register, RegisterDTO.class)).collect(Collectors.toList());
        logger.info("Get all registers successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), registerDTOList);
    }

    public Response<RegisterDTO> getRegisterById(Integer id) {
        logger.info("{}{}", GET_REGISTER_BY_ID, id);
        if (id == null) {
            logger.warn("{}{}", GET_REGISTER_BY_ID, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(!registerRepository.existsById(id)) {
            logger.warn("{}{}", GET_REGISTER_BY_ID, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Register register = registerRepository.findRegisterById(id);
        RegisterDTO registerDTO = modelMapper.map(register, RegisterDTO.class);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), registerDTO);
    }

    public Response<Void> createRegister(RegisterDTO registerDTO) {
        logger.info("{}{}", CREATE_REGISTER, registerDTO);
        if ((registerDTO.getStudentId() == null || !studentIdPattern.matcher(registerDTO.getStudentId()).find())
                || (registerDTO.getPersonalMail() == null || !mailPattern.matcher(registerDTO.getPersonalMail()).find())
                || (registerDTO.getSchoolMail() == null || !mailPattern.matcher(registerDTO.getSchoolMail()).find() || !registerDTO.getSchoolMail().endsWith(studentEmailDomain))) {
            logger.warn("{}{}", CREATE_REGISTER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(registerRepository.findByPersonalMailOrSchoolMail(registerDTO.getPersonalMail(), registerDTO.getSchoolMail()) != null || registerRepository.existsByStudentId(registerDTO.getStudentId())) {
            logger.warn("{}{}", CREATE_REGISTER, "Register is already exist");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Register is already exist");
        }
        Register register = modelMapper.map(registerDTO, Register.class);
        register.setId(null);
        logger.info("{}{}", CREATE_REGISTER, register);
        registerRepository.save(register);
        logger.info("Create register successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateRegister(RegisterDTO registerDTO) {
        logger.info("{}{}", UPDATE_REGISTER, registerDTO);
        Register register = registerRepository.findRegisterByStudentId(registerDTO.getStudentId());
        if(register == null) {
            logger.warn("{}{}", UPDATE_REGISTER, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (!studentIdPattern.matcher(registerDTO.getStudentId()).find()
                || !mailPattern.matcher(registerDTO.getPersonalMail()).find()
                || !mailPattern.matcher(registerDTO.getSchoolMail()).find()
                || !registerDTO.getSchoolMail().endsWith(studentEmailDomain)) {
            logger.warn("{}{}", UPDATE_REGISTER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(registerDTO.getName() != null) {
            register.setName(registerDTO.getName());
        }
        if(registerDTO.getMajor() != null) {
            register.setMajor(registerDTO.getMajor());
        }
        if(registerDTO.getPhone() != null) {
            register.setPhone(registerDTO.getPhone());
        }
        if(registerDTO.getPersonalMail() != null
                && mailPattern.matcher(registerDTO.getPersonalMail()).find()
                && !registerRepository.existsByPersonalMail(registerDTO.getPersonalMail())) {
            register.setPersonalMail(registerDTO.getPersonalMail());
        }
        if(registerDTO.getSchoolMail() != null
                && mailPattern.matcher(registerDTO.getSchoolMail()).find()
                && registerDTO.getSchoolMail().endsWith(studentEmailDomain)
                && !registerRepository.existsBySchoolMail(registerDTO.getSchoolMail())) {
            register.setSchoolMail(registerDTO.getSchoolMail());
        }
        if(registerDTO.getSemester() != null) {
            register.setSemester(registerDTO.getSemester());
        }
        registerRepository.save(register);
        logger.info("Update register successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteRegister(Integer id) {
        logger.info("{}{}", DELETE_REGISTER, id);
        if (id == null) {
            logger.warn("{}{}", DELETE_REGISTER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Register register = registerRepository.findRegisterById(id);
        if (register == null) {
            logger.warn("{}{}", DELETE_REGISTER, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        registerRepository.delete(register);
        logger.info("Delete register successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
