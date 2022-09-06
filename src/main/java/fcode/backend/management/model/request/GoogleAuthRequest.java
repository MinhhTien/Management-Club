package fcode.backend.management.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

@Getter
@NoArgsConstructor
@ToString
public class GoogleAuthRequest {

    public GoogleAuthRequest(String code) {
        this.code = code;
    }

    public GoogleAuthRequest(GoogleAuthRequest googleAuthRequest, String code,String redirectUri) {
        this.clientId = googleAuthRequest.clientId;
        this.clientSecret = googleAuthRequest.clientSecret;
        this.redirectUri = redirectUri;
        this.grantType = googleAuthRequest.grantType;
        this.code = code;
    }

    @JsonProperty("client_id")
    @Value("${auth.client.id}")
    private String clientId;
    @JsonProperty("client_secret")
    @Value("${auth.client.secret}")
    private String clientSecret;
    @JsonProperty("redirect_uri")
    private String redirectUri;
    @JsonProperty("grant_type")
    @Value("${auth.grant.type}")
    private String grantType;
    private String code;
}
