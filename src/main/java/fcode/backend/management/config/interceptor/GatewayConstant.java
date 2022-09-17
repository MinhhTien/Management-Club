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
        apiEntities.add(new ApiEntity("Auth api", "/auth/**", GET_METHOD, null));

        apiEntities.add(new ApiEntity("Challenge Get api", "/challenge/**", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Challenge Post api", "/challenge/**", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Challenge Put api", "/challenge/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Challenge Delete api", "/challenge/**", DELETE_METHOD, Role.MANAGER));

        apiEntities.add(new ApiEntity("Get All Subjects", "/subject/all", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Get Subjects By Semester", "/subject/semester/{semester:\\d+}", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Get One Subject", "/subject/{subjectId:\\d+}", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Get Subject By Name", "/subject/name/{name}", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Search Subjects", "/subject/search", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Create New Subject", "/subject", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Update Subject", "/subject", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Delete Subject", "/subject/{subjectId:\\d+}", DELETE_METHOD, Role.MANAGER));

        apiEntities.add(new ApiEntity("Get All Resources", "/resource/all", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Get Resources By Semester", "/resource/semester/{semester:\\d+}", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Get Resources By Subject", "/resource/subject/{subjectId:\\d+}", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Search Resources By Contributor", "/resource/contributor", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Get One Resource", "/resource/{resourceId:\\d+}", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Create New Resource", "/subject", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Update Resource", "/resource", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Delete Resource", "/resource/{resourceId:\\d+}", DELETE_METHOD, Role.MANAGER));

        apiEntities.add(new ApiEntity("Question Get api", "/question/**", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Question Post api", "/question/**", POST_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Question Put api", "/question/", PUT_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Question Put api", "/question/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Question Delete api", "/question/**", DELETE_METHOD, Role.MEMBER));

        apiEntities.add(new ApiEntity("Comment Get api", "/comment/**", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Comment Post api", "/comment/**", POST_METHOD, Role.STUDENT));
        apiEntities.add(new ApiEntity("Comment Put api", "/comment/**", PUT_METHOD, Role.STUDENT));
        apiEntities.add(new ApiEntity("Comment Delete api", "/comment/**", DELETE_METHOD, Role.STUDENT));

        apiEntities.add(new ApiEntity("Get All Announcements", "/announcement/all", GET_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Get One Announcement", "/announcement/{announcementId:\\d+}", GET_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Search Announcements By Title", "/announcement/search", GET_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Create New Announcement", "/announcement", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Update One Announcement", "/announcement", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Delete Announcement ", "/announcement/{announcementId:\\d+}", DELETE_METHOD, Role.MANAGER));

        apiEntities.add(new ApiEntity("Article Get api", "/article/**", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Article Post api", "/article/**", POST_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Article Put api", "/article", PUT_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Article Approve api", "/article/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Article Delete api", "/article/**", DELETE_METHOD, Role.MEMBER));
    }
}
