package fcode.backend.management.config.interceptor;

import fcode.backend.management.config.Role;

import java.util.ArrayList;
import java.util.List;

public class GatewayConstant {

    protected static final List<ApiEntity> apiEntities = new ArrayList<>();
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static final String PUT_METHOD = "PUT";
    private static final String DELETE_METHOD = "DELETE";

    private GatewayConstant() {
    }

    public static void addApiEntities() {

        apiEntities.add(new ApiEntity("Auth api","/auth/**",GET_METHOD, null));

        apiEntities.add(new ApiEntity("Question Get api", "/question/**",GET_METHOD,null));
        apiEntities.add(new ApiEntity("Question Post api", "/question/**",POST_METHOD, Role.STUDENT));
        apiEntities.add(new ApiEntity("Question Put api", "/question/**",PUT_METHOD,Role.STUDENT));
        apiEntities.add(new ApiEntity("Question Delete api", "/question/**",DELETE_METHOD,Role.STUDENT));

        apiEntities.add(new ApiEntity("Comment Get api", "/comment/**",GET_METHOD,null));
        apiEntities.add(new ApiEntity("Comment Post api", "/comment/**",POST_METHOD, Role.STUDENT));
        apiEntities.add(new ApiEntity("Comment Put api", "/comment/**",PUT_METHOD,Role.STUDENT));
        apiEntities.add(new ApiEntity("Comment Delete api", "/comment/**",DELETE_METHOD,Role.STUDENT));
    }
}
