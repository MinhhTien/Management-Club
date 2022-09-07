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

<<<<<<< HEAD
        apiEntities.add(new ApiEntity("Challenge Get api", "/challenge/**", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Challenge Post api", "/challenge/**", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Challenge Put api", "/challenge/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Challenge Delete api", "/challenge/**", DELETE_METHOD, Role.MANAGER));
=======
        apiEntities.add(new ApiEntity("Subject Get api", "/subject/**",GET_METHOD,null));
        apiEntities.add(new ApiEntity("Subject Post api", "/subject",POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Subject Put api", "/subject",PUT_METHOD,Role.MANAGER));
        apiEntities.add(new ApiEntity("Subject Delete api", "/subject/**",DELETE_METHOD,Role.MANAGER));

        apiEntities.add(new ApiEntity("Resource Get api", "/resource/**",GET_METHOD,null));
        apiEntities.add(new ApiEntity("Resource Post api", "/resource",POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Resource Put api", "/resource",PUT_METHOD,Role.MANAGER));
        apiEntities.add(new ApiEntity("Resource Delete api", "/resource/**",DELETE_METHOD,Role.MANAGER));
>>>>>>> e87e3499daa25c214ebb7ec111da45884c8a21a2
    }
}
