package fcode.backend.management.config.interceptor;

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
        apiEntities.add(new ApiEntity("Login by student","/auth",GET_METHOD, null));
        apiEntities.add(new ApiEntity("Login by member", "/auth", PUT_METHOD, null));
        apiEntities.add(new ApiEntity("Register", "/auth", POST_METHOD, null));
    }
}
