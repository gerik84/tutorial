package models;


import io.ebean.Ebean;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import java.util.UUID;

@Entity
public class Media extends ModerateModel {


    @Enumerated(EnumType.STRING)
    protected MediaType type;


    private String title = null;

    @Lob
    protected String url;

    @Lob
    protected String thumb;


    public enum MediaType {
        Image,
        Video,
        Html
    }

    public Media() {
    }

    public Media(String url) {
        //image assumed
        this.url = url;
        this.type = MediaType.Image;
    }

    public static Media getMediaByUrl(String url) {
        Media media = Ebean.find(Media.class).where().eq("url", url).setMaxRows(1).findUnique();
        if(media == null) {
            media = new Media(url);
            media.save();
        }
        return media;
    }

    public static Media getMedia(UUID id) {
        if(id == null) return null;
        return Ebean.find(Media.class).where().eq("id", id).findUnique();
    }

    public static Media getMedia(String id) {
        if (id == null) return null;
        return getMedia(UUID.fromString(id));
    }
    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
