package models;

import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by redline on 22.09.17.
 */
@Entity
public class GoodsTree extends UUIDBaseModel {

    @ManyToOne
    private Goods parent = null;

    @NotNull
    @ManyToOne
    private Goods children;

    public Goods getParent() {
        return parent;
    }

    public void setParent(Goods parent) {
        this.parent = parent;
    }

    public Goods getChildren() {
        return children;
    }

    public void setChildren(Goods children) {
        this.children = children;
    }
}
