package fcode.backend.management.service;

import fcode.backend.management.model.dto.FeeDTO;
import fcode.backend.management.model.dto.MemberDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.FeeRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Fee;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FeeService {
    @Autowired
    FeeRepository feeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(FeeService.class);
    private static final String CREATE_FEE = "Create fee: ";
    private static final String GET_FEE = "Get fee: ";
    private static final String UPDATE_FEE = "Update fee: ";
    private static final String DELETE_FEE = "Delete fee: ";
    private static final String CHARGE_MEMBER_FEE = "Charge fee for member: ";
    private static final String GET_MEMBER_NOT_PAY_FEE = "Get member not pay fee: ";
    public Response<Void> createFee(FeeDTO feeDTO) {
        logger.info("{}{}", CREATE_FEE, feeDTO);
        if(feeDTO == null) {
            logger.warn("{}{}",CREATE_FEE, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(feeRepository.findFeeByName(feeDTO.getName()) != null) {
            logger.warn("{}{}", CREATE_FEE, "Fee already exist.");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Fee already exist.");
        }
        Fee fee = modelMapper.map(feeDTO, Fee.class);
        fee.setId(null);
        feeRepository.save(fee);
        logger.info("Create free successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<FeeDTO> getFeeById(Integer id) {
        logger.info("{}{}", GET_FEE,id);
        if (id == null) {
            logger.warn("{}{}", GET_FEE, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if(!feeRepository.existsById(id)) {
            logger.warn("{}{}", GET_FEE, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        FeeDTO feeDTO = modelMapper.map(feeRepository.findFeeById(id), FeeDTO.class);
        logger.info("{}{}", GET_FEE, ServiceMessage.SUCCESS_MESSAGE.getMessage());
        return  new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), feeDTO);
    }
    @Transactional
    public Response<Set<FeeDTO>> getAllFee() {
        logger.info("{}All Fee", GET_FEE);
        Set<FeeDTO> feeDTOSet = feeRepository.getAllFees().stream()
                .map(fee -> modelMapper.map(fee, FeeDTO.class)).collect(Collectors.toSet());
        logger.info("Get all fees success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), feeDTOSet);
    }

    public Response<Void> updateFee(FeeDTO feeDTO) {
        logger.info("{}{}", UPDATE_FEE, feeDTO);
        if (feeDTO == null) {
            logger.warn("{}{}", UPDATE_FEE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        Fee fee = feeRepository.findFeeById(feeDTO.getId());
        if (fee == null) {
            logger.warn("{}{}", UPDATE_FEE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (feeDTO.getName() != null) {
            if (feeRepository.existsByName(feeDTO.getName())) {
                logger.warn("{}{}", UPDATE_FEE, "This fee already exists.");
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "This fee already exists.");
            }
            fee.setName(feeDTO.getName());
        }
        feeRepository.save(fee);
        logger.info("Update free successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteFee(Integer id) {
        logger.info("{}{}", DELETE_FEE, id);
        if (id == null) {
            logger.warn("{}{}", DELETE_FEE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if (!feeRepository.existsById(id)) {
            logger.warn("{}{}", DELETE_FEE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Fee fee =  feeRepository.findFeeById(id);
        if (!fee.getMembers().isEmpty()) {
            logger.warn("{}{}", DELETE_FEE, "There are some members pay for this fee.");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "There are some members pay for this fee.");
        }

        feeRepository.deleteById(id);
        logger.info("Delete fee successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> chargeMemberFee(String studentId, String feeName) {
        logger.info("{} student Id: {} feeName: {}", CHARGE_MEMBER_FEE, studentId, feeName);
        if (studentId == null || feeName == null) {
            logger.warn("{}{}", CHARGE_MEMBER_FEE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        studentId = studentId.trim();
        feeName = feeName.trim();
        if (!memberRepository.existsByStudentId(studentId) || !feeRepository.existsByName(feeName)) {
            logger.warn("{}{}", CHARGE_MEMBER_FEE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        Member member = memberRepository.findMemberByStudentId(studentId, Status.ACTIVE.toString());
        Fee fee = feeRepository.findFeeByName(feeName);
        member.getFees().add(fee);
        fee.getMembers().add(member);
        memberRepository.save(member);
        logger.info("Charge fee of member successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());

    }
    @Transactional
    public Response<Set<MemberDTO>> getMemberNotPayFee(String feeName) {
        logger.info("{}{}", GET_MEMBER_NOT_PAY_FEE, feeName);
        if (feeName == null) {
            logger.warn("{}{}", GET_MEMBER_NOT_PAY_FEE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        feeName = feeName.trim();
        if (!feeRepository.existsByName(feeName)) {
            logger.warn("{}{}", GET_MEMBER_NOT_PAY_FEE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Fee fee = feeRepository.findFeeByName(feeName);
        Set<Member> membersPayFee = fee.getMembers();
        Set<Member> membersNotPayFee = memberRepository.findALlMember(Status.ACTIVE.toString()).stream().filter(member -> !membersPayFee.contains(member)).collect(Collectors.toSet());
        Set<MemberDTO> memberDTOS = membersNotPayFee.stream().map(member -> modelMapper.map(member, MemberDTO.class)).collect(Collectors.toSet());
        logger.info("Get members who do not pay the the fee successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), memberDTOS);
    }
    @Transactional
    public Response<Set<FeeDTO>> getFeeMemberNotPay(String studentId) {
        logger.info("{}{}", GET_MEMBER_NOT_PAY_FEE, studentId);
        if (studentId == null) {
            logger.warn("{}{}", GET_MEMBER_NOT_PAY_FEE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if (!memberRepository.existsByStudentId(studentId)) {
            logger.warn("{}{}", GET_MEMBER_NOT_PAY_FEE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Set<Fee> feeMemberPay = memberRepository.findMemberByStudentId(studentId, Status.ACTIVE.toString()).getFees();
        Set<Fee> feeMemberNotPay = feeRepository.findAll().stream().filter(fee -> !feeMemberPay.contains(fee)).collect(Collectors.toSet());
        Set<FeeDTO> feeDTOSet = feeMemberNotPay.stream().map(fee -> modelMapper.map(fee, FeeDTO.class)).collect(Collectors.toSet());
        logger.info("Get fee that member do not pay");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), feeDTOSet);
    }

}
