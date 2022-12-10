package fcode.backend.management.config.interceptor;


import fcode.backend.management.config.JwtTokenUtil;
import fcode.backend.management.config.Role;
import fcode.backend.management.config.interceptor.exception.*;
import fcode.backend.management.model.dto.LoginUserDTO;
import fcode.backend.management.repository.MemberRepository;

import fcode.backend.management.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class GatewayInterceptor implements HandlerInterceptor {
    private static Logger logger = LogManager.getLogger(GatewayInterceptor.class);
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Max-Age", "3600");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD, OPTIONS");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization, X-Forwarded-For");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
        response.addHeader("Vary", "Origin, Access-Control-Request-Method, Access-Control-Request-Headers");
        try {
            LoginUserDTO loginUserDTO = verifyRequest(request);
            if (loginUserDTO != null) {
                if (loginUserDTO.getId() != null)
                    request.setAttribute("userId", loginUserDTO.getId());//get user email if request check success
                    request.setAttribute("userEmail", loginUserDTO.getEmail());
            }
            return true;
        } catch (AuthException e) {
            response.setStatus(e.getHttpStatus().value(),e.getMessage());
            logger.error("{} {}", e.getHttpStatus().value(), e.getMessage());
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private LoginUserDTO verifyRequest(HttpServletRequest request) throws RuntimeException {
        String httpMethod = request.getMethod();
        String servletPath = request.getServletPath();
        String accessToken = request.getHeader(GatewayConstant.AUTHORIZATION_HEADER);
        ApiEntity apiEntity = getMatchingAPI(httpMethod, servletPath);
        if (apiEntity.getRole() == null)
            return null;
        if (accessToken == null) {
            throw new NoTokenException();
        }
        String userEmail = jwtTokenUtil.getUserEmailFromToken(accessToken);
        if (userEmail == null) {
            throw new WrongTokenException();
        }
        if (apiEntity.getRole().equals(Role.STUDENT))
            return new LoginUserDTO(userEmail);
        LoginUserDTO loginUserDTO = memberRepository.getLoginUserByEmail(userEmail);
        logger.info("Ending-GetLoginUserByEmail");
        logger.info("Request Remote Address:{}",request.getRemoteAddr());
        logger.info("User IP Address:{}",loginUserDTO.getIp());
        if(loginUserDTO == null)
        {
            logger.warn("{}{}","Login member is not exist",userEmail);
            throw new ServiceException(HttpStatus.UNAUTHORIZED,"Please register and contact to F-Code club to be accepted");
        }
        if (!request.getRemoteAddr().equals(loginUserDTO.getIp()))
            throw new AccountLoggedInException();
        logger.info("Path:{} VerifyDTO:{}", servletPath, loginUserDTO);
        if (loginUserDTO.getRole().ordinal() >= apiEntity.getRole().ordinal())
        {
            loginUserDTO.setEmail(userEmail);
            return loginUserDTO;
        }
        else
            throw new NoAccessRoleException();
    }

    private ApiEntity getMatchingAPI(String httpMethod, String path) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (ApiEntity apiEntity : GatewayConstant.apiEntities) {
            if (matcher.match(apiEntity.getPattern(), path) && httpMethod.equals(apiEntity.getHttpMethod())) {
                logger.info("Found api matched");
                return apiEntity;
            }
        }
        throw new NotFoundApiException();
    }
}