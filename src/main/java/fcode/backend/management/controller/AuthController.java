package fcode.backend.management.controller;

import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping
public class AuthController {
    @Autowired
    AuthService authService;
    @Value("${auth.login.member}")
    private String loginByMemberUrl;
    @Value("${auth.login.student}")
    private String loginByStudentUrl;
    @Value("${auth.register}")
    private String registerUrl;
    @GetMapping("/login/student")
    public RedirectView loginByStudentRedirect() throws IOException {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(loginByStudentUrl);
        return redirectView;
    }
    @GetMapping("/login/member")
    public RedirectView loginByMemberRedirect(HttpServletResponse response) throws IOException {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(loginByMemberUrl);
        return redirectView;
    }
    @GetMapping("/register")
    public RedirectView registerRedirect(HttpServletResponse response) throws IOException {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(registerUrl);
        return redirectView;
    }
    @GetMapping("/auth/student")
    public Response<String> loginByStudent(@RequestParam String code, HttpServletRequest request) {
        return authService.loginByStudent(code,request.getRequestURL().toString());
    }
    @GetMapping("/auth/member")
    public Response<String> loginByMember(@RequestParam String code, HttpServletRequest request) {
        return authService.loginByMember(code,request.getRemoteAddr(), request.getRequestURL().toString());
    }
    @GetMapping("/auth/register")
    public Response<Void> register(@RequestParam String code,HttpServletRequest request){
        return authService.register(code,request.getRequestURL().toString());
    }
}
