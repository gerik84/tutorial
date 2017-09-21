package utils;

import models.App;
import models.Role.ROLE;
import models.User;
import play.mvc.Http;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static play.mvc.Http.HeaderNames.AUTHORIZATION;

/**
 * Created by pavel on 21.09.17.
 */
public class Auth {

    public static final String A_TOKEN_NAME = "access_token";
    public static final String APP_KEY_HEADER = "X-BLOG-APP-KEY";

    public static final ROLE[] u_guest = {ROLE.view};
    public static HashSet<ROLE> s_type_guest = new HashSet<>(Arrays.asList(u_guest));

    public static User getUser(Http.Request request, boolean allowFromCookie) {
        Optional<String> header = request.header(AUTHORIZATION);
        String token = header.isPresent() ? header.get() : null;

        if(token == null && allowFromCookie) {
            Http.Cookie c = request.cookie(A_TOKEN_NAME);
            if(c != null) {
                token = c.value();
            }
        }
        return User.getUserByToken(token);
    }

    public static App getApp(Http.Request request) {
        String appKey = request.getHeader(APP_KEY_HEADER);
        return App.getAppByKey(appKey);
    }
}
