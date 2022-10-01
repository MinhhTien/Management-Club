package fcode.backend.management.controller;

import fcode.backend.management.model.dto.FeeDTO;
import fcode.backend.management.model.dto.MemberDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping(value = "/fee")
@RestController
public class FeeController{
    @Autowired
    FeeService feeService;
    @PostMapping
    public Response<Void> createFee(@RequestBody FeeDTO feeDTO) {
        return feeService.createFee(feeDTO);
    }
    @PostMapping(value = "/member")
    public Response<Void> chargeMemberFee(@RequestParam String studentId, @RequestParam String feeName) {
        return feeService.chargeMemberFee(studentId, feeName);
    }
    @GetMapping("/{feeId}")
    public Response<FeeDTO> getFeeById(@PathVariable Integer feeId) {
        return feeService.getFeeById(feeId);
    }
    @GetMapping("/all")
    public Response<Set<FeeDTO>> getAllFee() {
        return feeService.getAllFee();
    }
    @GetMapping("/debt")
    public Response<Set<MemberDTO>> getMemberNotPayFee(@RequestParam String feeName) {
        return feeService.getMemberNotPayFee(feeName);
    }
    @GetMapping("/member/debt")
    public Response<Set<FeeDTO>> getFeeMemberNotPay(@RequestParam String studentId) {
        return feeService.getFeeMemberNotPay(studentId);
    }
    @PutMapping
    public Response<Void> updateFee(@RequestBody FeeDTO feeDTO) {
        return feeService.updateFee(feeDTO);
    }
    @DeleteMapping(value = "/{feeId}")
    public Response<Void> deleteFee(@PathVariable Integer feeId) {
        return feeService.deleteFee(feeId);
    }

}
