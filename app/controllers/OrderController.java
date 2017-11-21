package controllers;

import akka.actor.ActorSystem;
import com.google.inject.Inject;
import models.Order;
import models.User;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;

/**
 * Created by pavel on 21.11.17.
 */
public class OrderController extends BaseController {

    @Inject
    public OrderController(ActorSystem actorSystem, ExecutionContextExecutor exec) {
        super(actorSystem, exec);
    }

    public Result getOrder(java.util.UUID orderID) {
        User user = getUser();
        if (user == null) {
            return unauthorized();
        }
        Order order = getModelUnsafe(Order.class, orderID, q -> {

        });
        return makeResult(order, user);
    }


    public Result getOrders() {
        User user = getUser();
        if (user == null) {
            return unauthorized();
        }

        return getListUnsafe(user, Order.class, q -> {
            q.orderBy("whenUpdated DESC");
        });
    }
}
