package controllers;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import files.FileStorageSystem;
import files.FileStorageType;
import io.ebean.Ebean;
import io.ebean.PagedList;
import models.*;
import org.joda.time.DateTime;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;
import utils.Auth;
import utils.Const;
import utils.Mixin;
import utils.ModelLists;

import javax.annotation.Nullable;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static utils.Const.APPLICATION_JSON;

/**
 * Created by pavel on 21.09.17.
 */
abstract class BaseController extends Controller {

    protected final ActorSystem actorSystem;
    protected final ExecutionContextExecutor exec;
    protected final FileStorageSystem fileStorageSystem;

    public BaseController(ActorSystem actorSystem, ExecutionContextExecutor exec) {
        this.actorSystem = actorSystem;
        this.exec = exec;
        this.fileStorageSystem = FileStorageType.DATABASE.initStorage();
        fileStorageSystem.setActorSystem(actorSystem);
//        eventSystem = new EventStreamSystem(actorSystem);
    }

    User getUser() {
        return getUser(false);
    }

    protected User getUser(boolean allowFromCookie) {
        return Auth.getUser(request(), allowFromCookie);
    }

    protected App getApp() {
        return Auth.getApp(request());
    }

    Result makeResult(Object obj, User user) {
        ObjectMapper mapper = Mixin.createMapper(user);
        try {
            return ok(mapper.writeValueAsString(obj)).as(APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return internalServerError();
        }
    }


    <T extends UUIDBaseModel> Result createUnsafe(Class<T> model, @Nullable User user) {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data");
        }
        T np = createUnsafeFromJson(json, model, user);
        return makeResult(np, user);
    }

    private <T extends UUIDBaseModel> T createUnsafeFromJson(JsonNode json, Class<T> model, @Nullable User user) {
        try {
//            logAction("create", user, model.getClass().getName(), json);
            ObjectMapper mapper = new ObjectMapper();
            T np = mapper.treeToValue(json, model);
            if(np == null || !np.checkMinimumProperties()) {
                Logger.error("createUnsafeFromJson: bad model: "+json);
                return null;
            }

            Ebean.execute(() -> {
                if(!np.checkUniqueFields()) {
                    return;
                }
                np.postCreation();
                np.save();
            });
            if(np.getId() == null) {
                Logger.error("createUnsafeFromJson: conflict");
                return null;
            }
            np.refresh();
            return np;
        } catch (JsonParseException e) {
            Logger.error("createUnsafeFromJson: ", e);
            return null;
        } catch (IOException e) {
            // shouldn't really happen
            Logger.error("createUnsafeFromJson: ", e);
            return null;
        }
    }

    protected boolean isClient(User user) {
        return  user == null;
    }

    protected <T extends UUIDBaseModel> Result getListUnsafe(User user, Class<T> model, ModelLists.ICustomQuery q) {

        if(request().getQueryString(ModelLists.countParam) != null) {
            int count = ModelLists.getListCountFromRequest(model, request(), q, isClient(user));
            return ok(Json.newObject().put(ModelLists.countParam, count));
        }

        //List<T> list = ModelLists.getListFromRequest(model, request(), q, isClient(user));
        PagedList<T> list = ModelLists.getListFromRequest(model, request(), q, isClient(user));
        list.loadCount();
        Result res = getResultListUnsafe(user, model, list.getList());
        return res.withHeader(ModelLists.TOTAL_COUNT_HEADER, String.valueOf(list.getTotalCount()));
    }

    protected <T extends UUIDBaseModel> Result getResultListUnsafe(User user, Class<T> model, List<T> list) {
        if(request().hasHeader(IF_MODIFIED_SINCE) && list.isEmpty()) {
            return status(NOT_MODIFIED);
        }
        ObjectMapper mapper = Mixin.createMapper(user, model, request().acceptLanguages());
        Timestamp lastModfied;
        Long lastUpdate = ModelLists.lastUpdate(model);
        if(lastUpdate != null) {
            lastModfied = new Timestamp(lastUpdate);
        } else {
            lastModfied = new Timestamp(System.currentTimeMillis());
        }
        DateTime time = new DateTime(lastModfied);
        Logger.debug("lu = "+lastUpdate);
        try {
            return ok(mapper.writeValueAsString(list)).as("application/json").withHeader(LAST_MODIFIED, time.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return internalServerError();
        }

    }

    public static HashSet<Class> includeMethodsFrom;
    static {
        includeMethodsFrom = new HashSet<>();
        includeMethodsFrom.add(ModerateModel.class);
    }

    protected <T extends UUIDBaseModel> Result modifyByUser(Class<T> model, UUID id, HashSet<Role.ROLE> allowedTypes) {
        User user = getUser();
        if(user == null) {
            return forbidden();
        }
        if(!Auth.isAllowedForUser(user, allowedTypes)) {
            return status(FORBIDDEN);
        }
        T em = T.getByID(model, id);
        if(em == null) {
            return notFound();
        }
        return modifyUnsafe(em, user);
    }

    private <T extends UUIDBaseModel> Result modifyUnsafe(T em, @Nullable User user) {
        JsonNode json = request().body().asJson();

        if (json == null) {
            return badRequest("Expecting Json data");
        }
        if(em == null) {
            return notFound();
        }
        try {
            logAction("modify", user != null?user:em, em.getClass().getName(), json);
            ObjectMapper mapper = new ObjectMapper();
            T np = mapper.treeToValue(json, (Class<T>) em.getClass());
            Object pre = em.preModify();
            //*
            if(np instanceof ModerateModel) {
                ModerateModel npm = (ModerateModel)np;
                ModerateModel emm = (ModerateModel)em;
//                if(user != null && user.isAdmin() && npm.getStatus() != null) {
//                    emm.setStatus(npm.getStatus());
//                    if(emm.getStatus().equals(ModerateModel.Status.BLOCKED)) {
//                        emm.setBlockingReason(npm.getBlockingReason());
//                    }
//                } else {
                    if(npm.needSwitchToPending()) {
                        npm.setStatus(ModerateModel.Status.PENDING);
                    }
//                }
            }
            //*/

            Mixin.merge(em, np, includeMethodsFrom, np.excludedMethods());
            if(!em.checkUniqueFields()) {
                return status(CONFLICT);
            }
            em.beforeUpdate(actorSystem, pre);
            em.update();
            em.postModify(actorSystem, pre);
            em.refresh();
            mapper = Mixin.createMapper(user, em.getClass(), request().acceptLanguages());
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return ok(mapper.writeValueAsString(em)).as("application/json");
        } catch (JsonParseException e) {
            return badRequest("bad jackson");
        } catch (IOException e) {
            e.printStackTrace();
            return badRequest("IO");
        }

    }

    protected <T extends UUIDBaseModel> Result deleteUnsafe(Class<T> model, User user, UUID id) {
        T em = T.getByID(model, id);
        if(em == null) {
            return notFound();
        }
        logAction("delete", user, em.getClass().getName(), null);
        em.m_delete();
        return noContent();
    }

    protected void logAction(String action, Object modifier, String modelName, JsonNode json) {
        if(Const.logging_changes) {
            Logger.info(String.format("%s by %s: object (%s) got %s", action, Objects.toString(modifier), modelName, Objects.toString(json)));
        }
    }



}
