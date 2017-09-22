package models;

import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;

/**
 * Created by redline on 22.09.17.
 */
@Entity
public class Goods extends ModerateModel {

    @NotNull
    private String name;

    @NotNull
    private String model;

    private String description;

    @OneToOne
    private Media cover;

    @ManyToMany(cascade = ALL)
    private List<Media> images;

    @OneToOne
    private Brand brand;

    @OneToMany(cascade = PERSIST)
    private List<Property> properties;

}
