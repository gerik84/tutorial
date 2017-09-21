package models;

import io.ebean.Ebean;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by pavel on 21.09.17.
 */
@Entity
public class App extends ModerationModel {

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public enum TYPE {
        WEB, MOBILE
    }

    @Enumerated(EnumType.STRING)
    private TYPE type;

    private String name;

    private String apiKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public static App getAppByKey(String key) {
        if(key == null) return null;
        return Ebean.find(App.class).where().eq("apiKey", key).eq("status", "APPROVED").findUnique();
    }
}
