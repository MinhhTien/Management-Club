package fcode.backend.management.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GoogleInfoResponse {
    @JsonProperty
    private String id;
    @JsonProperty
    private String email;
    @JsonProperty("verified_email")
    private boolean verifiedEmail;
    @JsonProperty
    private String name;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("family_name")
    private String familyName;
    @JsonProperty
    private String picture;
}
