package controllers;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import io.ebean.Ebean;
import models.Goods;
import models.GoodsTree;
import models.User;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Created by redline on 25.09.17.
 */
public class GoodsController extends BaseController {


    @Inject
    public GoodsController(ActorSystem actorSystem, ExecutionContextExecutor exec) {
        super(actorSystem, exec);
    }

    public Result createGoods(UUID parentID) {
        User user = getUser();

        Goods parent = null;
        if (parentID != null) {
            parent = Goods.getByID(Goods.class, parentID);
        }

        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data");
        }

        Goods goods = createUnsafeFromJson(json, Goods.class, user);
        if (goods == null) {
            return internalServerError("Error created goods");
        }

        GoodsTree tree = new GoodsTree();
        tree.setParent(parent);
        tree.setChildren(goods);
        tree.save();

        return makeResult(goods, user);
    }

    public Result getCatalog(UUID parentID) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, String[]> stringMap = request().queryString();
        User user = getUser();
        if (user == null) {
            return unauthorized();
        }
        return getListUnsafe(user, Goods.class, q -> {
            if (parentID == null) {
                stringMap.forEach((key, params) -> {
                    q.in("properties.name", Arrays.asList(params));
                    q.eq("properties.property.name", key);
                });
                q.in("id", Ebean.createQuery(GoodsTree.class).select("children.id").where().isNull("parent").query());
            } else {
                q.in("id", Ebean.createQuery(GoodsTree.class).select("children.id").where().eq("parent.id", parentID).query());
            }
        });
    }
}