package fcode.backend.management.sevice;

import fcode.backend.management.config.WebMvcConfiguration;
import fcode.backend.management.model.request.GoogleAuthRequest;
import fcode.backend.management.model.response.GoogleInfoResponse;
import fcode.backend.management.model.response.GoogleTokenResponse;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.sevice.constant.ServiceMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
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

    private static final Logger logger = LogManager.getLogger(AuthService.class);
    private static final String CREATE_USER_MESSAGE = "Create user: ";
    private static final String VALIDATE_GG_TOKEN_MESSAGE = "Validate google token: ";
//    public Response<Void> createUser(String loginCode){
//
//    }
    public GoogleTokenResponse validateGoogleToken(String code) {
        if (code == null) {
            logger.warn("{}{}", VALIDATE_GG_TOKEN_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return null;
        }
        GoogleTokenResponse response = WebMvcConfiguration.getWebClientBuilder().build().post().uri(getTokenUrl).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(new GoogleAuthRequest(googleAuthRequest,code))).retrieve().bodyToMono(GoogleTokenResponse.class).block();
        logger.info("{}{}","Access token: ", response.toString());
        logger.info("{}{}","Token info: ",getGoogleInfoResponse(response.getAccessToken()));
        return response;
    }

    private String getGoogleInfoResponse(String token) {
        return WebMvcConfiguration.getWebClientBuilder().build().get().uri(infoTokenUrl).header("Authorization","Bearer " + token)
                .retrieve().bodyToMono(String.class).block();
    }

}
