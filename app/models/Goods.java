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

    @ManyToMany
    private List<PropertyItem> properties;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Media getCover() {
        return cover;
    }

    public void setCover(Media cover) {
        this.cover = cover;
    }

    public List<Media> getImages() {
        return images;
    }

    public void setImages(List<Media> images) {
        this.images = images;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<PropertyItem> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyItem> properties) {
        this.properties = properties;
    }
}
