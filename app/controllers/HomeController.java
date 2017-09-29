package controllers;

import models.Goods;
import models.GoodsTree;
import models.Property;
import models.PropertyItem;
import play.mvc.*;

import java.util.Arrays;
import java.util.Collections;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        Property property = new Property();
        property.setName("brand");
        property.save();

        PropertyItem item = new PropertyItem();
        item.setProperty(property);
        item.setName("Asus");
        item.save();

        PropertyItem item2 = new PropertyItem();
        item2.setName("MSI");
        item2.setProperty(property);
        item2.save();

        Goods goods = new Goods();
        goods.setName("Motheboard");
        goods.setModel("CT-410");
        goods.setProperties(Arrays.asList(item2));
        goods.save();

        GoodsTree tree = new GoodsTree();
        tree.setParent(null);
        tree.setChildren(goods);
        tree.save();


        return ok(views.html.index.render());
    }

    public Result test() {
        return status(OK, "OK");
    }

}
