package fcode.backend.management.controller;

import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @GetMapping("/student")
    public Response<String> loginByStudent(@RequestParam String code, HttpServletRequest request) {
        return authService.loginByStudent(code,request.getRequestURL().toString());
    }
    @GetMapping("/member")
    public Response<String> loginByMember(@RequestParam String code, HttpServletRequest request) {
        return authService.loginByMember(code,request.getRemoteAddr(), request.getRequestURL().toString());
    }
    @GetMapping("/register")
    public Response<Void> register(@RequestParam String code,HttpServletRequest request){
        return authService.register(code,request.getRequestURL().toString());
    }
}
