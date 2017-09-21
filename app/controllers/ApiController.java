package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.App;
import models.Role;
import models.Session;
import models.User;
import play.mvc.Result;
import utils.Auth;

import java.util.UUID;

import static play.libs.Json.newObject;

/**
 * Created by pavel on 21.09.17.
 */
public class ApiController extends BaseController {

    public Result signup() {
        App app = getApp();
        if (app == null) {
            return unauthorized();
        }
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data: " + request().body());
        }

        User user = new User();
        String s = json.findPath("name").textValue();
        String email = json.findPath("email").textValue();

        user.setId(UUID.randomUUID());
        user.setLogin(user.getId().toString());
        user.setAppID(app.getId());

        user.setRoles(Role.getRoles(Auth.s_type_guest));
        user.setType(User.TYPE.WEB_CLIENT);
        user.save();

        Session.makeSession(user.getId().toString(), user);
        ObjectNode result = newObject();
        result.put("accessToken", user.getId().toString());

        return ok(result);
    }


}
