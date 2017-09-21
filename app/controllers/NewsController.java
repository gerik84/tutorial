package controllers;

import akka.actor.ActorSystem;
import com.google.inject.Inject;
import io.ebean.Ebean;
import models.News;
import models.User;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;
import utils.Auth;

import java.util.List;
import java.util.UUID;

/**
 * Created by pavel on 21.09.17.
 */
public class NewsController extends BaseController {

    @Inject
    public NewsController(ActorSystem actorSystem, ExecutionContextExecutor exec) {
        super(actorSystem, exec);
    }

    public Result deleteNews(UUID id) {
        User user = getUser();
        return deleteUnsafe(News.class, user, id);
    }

    public Result modifyNews(UUID id) {
        return modifyByUser(News.class, id, Auth.s_moderator);
    }

    public Result createNews() {
        User user = getUser(false);
        if (!Auth.isAllowedForUser(user, Auth.s_moderator)) {
            return forbidden();
        }

        return createUnsafe(News.class, user);
    }

    public Result getNews() {
        User user = getUser(false);
        if (!Auth.isAllowedForUser(user, Auth.s_type_guest)) {
            return forbidden();
        }

        return getListUnsafe(user, News.class, null);

//        List<News> list = Ebean.find(News.class).where().findList();
//        return makeResult(list, null);
    }



}
