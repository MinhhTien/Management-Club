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
        apiEntities.add(new ApiEntity("Login api", "/login/**", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Register api", "/register", GET_METHOD, null));

        apiEntities.add(new ApiEntity("Challenge Get api", "/challenge/**", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Challenge Post api", "/challenge/**", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Challenge Put api", "/challenge/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Challenge Delete api", "/challenge/**", DELETE_METHOD, Role.MANAGER));

        apiEntities.add(new ApiEntity("Member Get api for member", "/member/verifySMail/*", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Member Get api for member", "/member/verifyPMail/*", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Member Get api for manager", "/member/**", GET_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Member Put api for member", "/member/us", PUT_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Member Put api for admin", "/member/ad", PUT_METHOD, Role.ADMIN));
        apiEntities.add(new ApiEntity("Member Delete api", "/member/**", DELETE_METHOD, Role.ADMIN));

        apiEntities.add(new ApiEntity("Event Get api", "/event/**", GET_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Event Post api", "/event/**", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Event Put api", "/event/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Event Delete api", "/event/**", DELETE_METHOD, Role.MANAGER));

        apiEntities.add(new ApiEntity("Get All Subjects", "/subject/all", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Get Subjects By Semester", "/subject/semester/{semester:\\d+}", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Get One Subject", "/subject/one/{subjectId:\\d+}", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Get Subject By Name", "/subject/name/{name}", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Search Subjects", "/subject/search", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Create New Subject", "/subject", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Update Subject", "/subject", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Delete Subject", "/subject/one/{subjectId:\\d+}", DELETE_METHOD, Role.MANAGER));

        apiEntities.add(new ApiEntity("Get All Resources", "/resource/all",GET_METHOD,null));
        apiEntities.add(new ApiEntity("Get Resources By Semester", "/resource/semester/{semester:\\d+}",GET_METHOD, null));
        apiEntities.add(new ApiEntity("Get Resources By Subject", "/resource/subject/{subjectId:\\d+}",GET_METHOD,null));
        apiEntities.add(new ApiEntity("Search Resources By Contributor", "/resource/contributor",GET_METHOD,null));
        apiEntities.add(new ApiEntity("Get One Resource", "/resource/one/{resourceId:\\d+}",GET_METHOD,null));
        apiEntities.add(new ApiEntity("Create New Resource", "/resource",POST_METHOD,Role.MANAGER));
        apiEntities.add(new ApiEntity("Update Resource", "/resource",PUT_METHOD,Role.MANAGER));
        apiEntities.add(new ApiEntity("Delete Resource", "/resource/one/{resourceId:\\d+}",DELETE_METHOD,Role.MANAGER));

        apiEntities.add(new ApiEntity("Question Get all questions api", "/question/questions", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Question Get questions by id api", "/question/{questionId:\\d+}", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Question Get questions by author api", "/question/author", GET_METHOD, Role.STUDENT));
        apiEntities.add(new ApiEntity("Question Get processing questions", "/question/processing", GET_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Question Get inactive questions", "/question/inactive", GET_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Question Post api", "/question/**", POST_METHOD, Role.STUDENT));
        apiEntities.add(new ApiEntity("Question Put api", "/question/", PUT_METHOD, Role.STUDENT));
        apiEntities.add(new ApiEntity("Question approve api", "/question/approve/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Question disapprove api", "/question/disapprove/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Question Delete api", "/question/**", DELETE_METHOD, Role.STUDENT));

        apiEntities.add(new ApiEntity("Comment Get api", "/comment/**", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Comment Post api", "/comment/**", POST_METHOD, Role.STUDENT));
        apiEntities.add(new ApiEntity("Comment Put api", "/comment/**", PUT_METHOD, Role.STUDENT));
        apiEntities.add(new ApiEntity("Comment Delete api", "/comment/{commentId:\\d+}", DELETE_METHOD, Role.STUDENT));
        apiEntities.add(new ApiEntity("Comment Delete api", "/comment/author/**", DELETE_METHOD, Role.MANAGER));

        apiEntities.add(new ApiEntity("Attendance Get by member api", "/attendance/**", GET_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Attendance Post api", "/attendance/**", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Attendance Put api", "/attendance/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Attendance Delete api", "/attendance/**", DELETE_METHOD, Role.MANAGER));
        
        apiEntities.add(new ApiEntity("Get All Announcements", "/announcement/all", GET_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Get One Announcement", "/announcement/one/{announcementId:\\d+}", GET_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Search Announcements By Title", "/announcement/search", GET_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Create New Announcement", "/announcement", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Update One Announcement", "/announcement", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Delete Announcement ", "/announcement/one/{announcementId:\\d+}", DELETE_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Get All Notifications By Member", "/announcement/notifications", GET_METHOD, Role.MEMBER));

        apiEntities.add(new ApiEntity("Article Get all article api", "/article/all", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Article Get article by id api", "/article/{id:\\d+}", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Article Get article by author api", "/article/author", GET_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Article Get processing articles", "/article/processing", GET_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Article Get inactive articles", "/article/inactive", GET_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Article Post api", "/article/**", POST_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Article Put api", "/article", PUT_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Article Approve api", "/article/approve/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Article Disapprove api", "/article/disapprove/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Article Delete api", "/article/**", DELETE_METHOD, Role.MEMBER));

        apiEntities.add(new ApiEntity("Plus point Get api", "/pluspoint/**", GET_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Plus point Post api", "/pluspoint/**", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Plus point Put api", "/pluspoint", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Plus point Delete api", "/pluspoint/**", DELETE_METHOD, Role.MANAGER));

        apiEntities.add(new ApiEntity("Position Get Api", "/position/**", GET_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Position Post Api", "/position/**", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Position Put Api", "/position/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Position Delete Api", "/position/**", DELETE_METHOD, Role.MANAGER));

        apiEntities.add(new ApiEntity("Crew Get api", "/crew/**", GET_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Crew Post api", "/crew/**", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Crew Put api", "/crew/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Crew Delete api", "/crew/**", DELETE_METHOD, Role.MANAGER));

        apiEntities.add(new ApiEntity("Crew Announcement Get api", "/announcement/crew/**", GET_METHOD, Role.MEMBER));
        apiEntities.add(new ApiEntity("Crew Announcement Post api", "/announcement/crew/**", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Crew Announcement Put api", "/announcement/crew/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Crew Announcement Delete api", "/announcement/crew/**", DELETE_METHOD, Role.MANAGER));

        apiEntities.add(new ApiEntity("Notification Get api", "/template", GET_METHOD, null));
        apiEntities.add(new ApiEntity("Notification Post api", "/notification/**", POST_METHOD, null));

        apiEntities.add(new ApiEntity("Fee Get api", "/fee/**", GET_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Fee Post api", "/fee/**", POST_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Fee Put api", "/fee/**", PUT_METHOD, Role.MANAGER));
        apiEntities.add(new ApiEntity("Fee Delete api", "/fee/**", DELETE_METHOD, Role.MANAGER));
    }
}
