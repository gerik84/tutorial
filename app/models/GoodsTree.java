package models;

import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by redline on 22.09.17.
 */
@Entity
public class GoodsTree extends UUIDBaseModel {

    private Goods parent = null;

    @NotNull
    @ManyToOne
    private Goods children;

}
