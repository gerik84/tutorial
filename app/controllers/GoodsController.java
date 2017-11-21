package controllers;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import io.ebean.Query;
import models.*;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Created by redline on 25.09.17.
 */
public class GoodsController extends BaseController {


    @Inject
    public GoodsController(ActorSystem actorSystem, ExecutionContextExecutor exec) {
        super(actorSystem, exec);
    }

    public Result getGoodsDetails(UUID goodsID) {
        User user = getUser();
//        if (user == null) {
//            return unauthorized();
//        }
        Goods byID = Goods.getByID(Goods.class, goodsID);
//        Goods order = getModelUnsafe(Goods.class, goodsID, q -> {
//
//        });
        return makeResult(byID, user);
    }

    public Result getProperty(UUID parentID) {
        List<Goods> allGoods;
        ExpressionList<Goods> where = Ebean.find(Goods.class)
                .select("properties")
                .where()
                .isNotNull("properties");
        if (parentID == null) {
            where.in("id", Ebean.createQuery(GoodsTree.class).select("children.id").where().isNull("parent").query());
        } else {
            where.in("id", Ebean.createQuery(GoodsTree.class).select("children.id").where().in("parent.id").query());
        }
        allGoods = where.findList();
        List<Property> properties = new ArrayList<>();
        HashMap<Property, List<PropertyItem>> itemHashMap = new HashMap<>();
        allGoods.parallelStream().forEach(item -> {
            if (item.getProperties() == null) {
                return;
            }

            item.getProperties().forEach(i -> {
                List<PropertyItem> propertyItems = itemHashMap.get(i.getProperty());
                if (propertyItems == null) {
                    propertyItems = new ArrayList<>();
                }
                propertyItems.add(i);
                itemHashMap.put(i.getProperty(), propertyItems);
            });

        });

        itemHashMap.forEach((property, propertyItems) -> {
            property.setItems(propertyItems);
            properties.add(property);
        });

        return makeResult(properties, null);
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