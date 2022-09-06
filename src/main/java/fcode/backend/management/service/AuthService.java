package fcode.backend.management.service;

import fcode.backend.management.config.JwtTokenUtil;
import fcode.backend.management.config.WebMvcConfiguration;
import fcode.backend.management.model.dto.LoginUserDTO;
import fcode.backend.management.model.request.GoogleAuthRequest;
import fcode.backend.management.model.response.GoogleInfoResponse;
import fcode.backend.management.model.response.GoogleTokenResponse;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;

@Service
public class AuthService {
    @Autowired
    GoogleAuthRequest googleAuthRequest;
    @Autowired
    MemberRepository memberRepository;
    @Value("${auth.token.url}")
    private String getTokenUrl;
    @Value("${auth.info.url}")
    private String infoTokenUrl;
    @Value("${student.email.domain}")
    private String studentEmailDomain;
    private static final Logger logger = LogManager.getLogger(AuthService.class);
    private static final String CREATE_USER_MESSAGE = "Create user: ";
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    private static final String VALIDATE_GG_TOKEN_MESSAGE = "Validate google token: ";
    public Response<String> loginByStudent(String loginCode,String redirectUri){
        GoogleTokenResponse googleTokenResponse = getGoogleToken(loginCode,redirectUri);
        logger.info("Login started");
        GoogleInfoResponse response = getGoogleInfoResponse(googleTokenResponse.getAccessToken());
        if(!response.getEmail().endsWith(studentEmailDomain))
        {
            logger.warn("Login user is not FPT student");
            throw new ServiceException(HttpStatus.UNAUTHORIZED,"Please login with FPT student email");
        }
        logger.info("{}{}","Login by student success: ", response.getEmail());
        return new Response<>(HttpStatus.OK.value(),"Login success", jwtTokenUtil.generateToken(response.getEmail()));
    }
    @Transactional
    public Response<String> loginByMember(String loginCode,String ip,String redirectUri){
        GoogleTokenResponse googleTokenResponse = getGoogleToken(loginCode,redirectUri);
        logger.info("Login by member started");
        GoogleInfoResponse response = getGoogleInfoResponse(googleTokenResponse.getAccessToken());
        LoginUserDTO loginUserDTO = memberRepository.getLoginUserByEmail(response.getEmail());
        if(loginUserDTO==null)
        {
            logger.warn("{}{}","Login member is not exist",response.getEmail());
            throw new ServiceException(HttpStatus.UNAUTHORIZED,"Please register and contact to F-Code club to be accepted");
        }
        memberRepository.updateIpByEmail(ip,response.getEmail());
        logger.info("{}{}","Login by member success: ", response.getEmail());
        return new Response<>(HttpStatus.OK.value(),ServiceMessage.SUCCESS_MESSAGE.getMessage(), jwtTokenUtil.generateToken(response.getEmail()));
    }
    public Response<Void> register(String loginCode,String redirectUri){
        GoogleTokenResponse googleTokenResponse = getGoogleToken(loginCode,redirectUri);
        logger.info("Member register started");
        GoogleInfoResponse response = getGoogleInfoResponse(googleTokenResponse.getAccessToken());
        if(memberRepository.existsByEmail(response.getEmail())!=null)
        {
            logger.warn("Member {} has already registered",response.getEmail());
            throw new ServiceException(HttpStatus.UNAUTHORIZED,"You have already registered, please contact to F-Code club to be accepted");
        }
        memberRepository.save(new Member(response,studentEmailDomain));
        logger.info("Member register success: {}", response.getEmail());
        return new Response<>(HttpStatus.OK.value(),ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
    public GoogleTokenResponse getGoogleToken(String code, String redirectUri) {
        if (code == null) {
            logger.warn("{}{}", VALIDATE_GG_TOKEN_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            throw new ServiceException(HttpStatus.BAD_REQUEST,ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        GoogleTokenResponse response = WebMvcConfiguration.getWebClientBuilder().build().post().uri(getTokenUrl).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(new GoogleAuthRequest(googleAuthRequest,code,redirectUri))).retrieve().bodyToMono(GoogleTokenResponse.class).block();
        logger.info("Get google token success");
        return response;
    }

    private GoogleInfoResponse getGoogleInfoResponse(String token) {
        return WebMvcConfiguration.getWebClientBuilder().build().get().uri(infoTokenUrl).header("Authorization","Bearer " + token)
                .retrieve().bodyToMono(GoogleInfoResponse.class).block();
    }

    public LoginUserDTO getLoginUserByEmail(String userEmail){
        return memberRepository.getLoginUserByEmail(userEmail);
    }

}
