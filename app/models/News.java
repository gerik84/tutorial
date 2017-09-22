package models;

import io.ebean.annotation.Index;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pavel on 21.09.17.
 */
@Entity
public class News extends ModerateModel {

    @Index
    private String title;

    private String shortDescription;

    @Column(length = 4096)
    private String description;

    @ManyToMany
    private List<Media> images;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Media> getImages() {
        return images;
    }

    public void setImages(List<Media> images) {
        this.images = images;
    }
}
