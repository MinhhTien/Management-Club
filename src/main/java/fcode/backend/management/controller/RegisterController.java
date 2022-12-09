package fcode.backend.management.controller;

import fcode.backend.management.model.dto.RegisterDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.RegisterService;
import fcode.backend.management.service.constant.ServiceMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/register-challenge")
public class RegisterController {
    @Autowired
    RegisterService registerService;

    @GetMapping(value = "/all")
    public Response<List<RegisterDTO>> getAllRegisters() {
        return registerService.getAllRegisters();
    }

    @GetMapping(value = "/id/{id}")
    public Response<RegisterDTO> getRegisterById(@PathVariable Integer id) {
        return registerService.getRegisterById(id);
    }

    @PostMapping(value = "/new")
    public Response<Void> createRegister(@RequestBody RegisterDTO registerDTO, @RequestAttribute(required = false) String userEmail) {
        try {
            if(!registerDTO.getSchoolMail().equals(userEmail)) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Student email is not matched");
            }
            return registerService.createRegister(registerDTO);
        } catch (Exception e) {
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
    }

    @PutMapping
    public Response<Void> updateRegister(@RequestBody RegisterDTO registerDTO) {
        return registerService.updateRegister(registerDTO);
    }

    @DeleteMapping(value = "/id/{id}")
    public Response<Void> deleteRegister(@PathVariable Integer id) {
        return registerService.deleteRegister(id);
    }
}
