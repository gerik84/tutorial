package controllers;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.*;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;
import utils.Auth;

import java.util.Optional;
import java.util.UUID;

import static play.libs.Json.newObject;

/**
 * Created by pavel on 21.09.17.
 */
public class ApiController extends BaseController {

    @Inject
    public ApiController(ActorSystem actorSystem, ExecutionContextExecutor exec) {
        super(actorSystem, exec);
    }

    public Result upload() {
        User user = getUser();
        if(user == null) {
            return unauthorized();
        }

        Optional<String> contentTypeHeader = request().getHeaders().get(CONTENT_TYPE);
        String ctype = contentTypeHeader.isPresent() ? contentTypeHeader.get() : null;
        if (ctype == null || !ctype.startsWith("image")) {
            return badRequest("only images can be uploaded");
        }

        String id = fileStorageSystem.upload(ctype, request());
        if (id == null) {
            return internalServerError();
        }
        Media media = new Media();
        media.setThumb("file/" + id + "_thumb");
        media.setUrl("file/" + id);
        media.setType(Media.MediaType.Image);
        media.save();
        return makeResult(media, user);
    }

    public Result download(String id) {
        if (id == null) {
            return badRequest("bad id of file");
        }
        return fileStorageSystem.download(id, request(), response());
    }

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
