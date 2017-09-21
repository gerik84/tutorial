package models;

import io.ebean.Ebean;


import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Created by pavel on 21.09.17.
 */
@Entity
@Table(name="users")
public class User extends ModerationModel {

    public UUID getAppID() {
        return appID;
    }

    public void setAppID(UUID appID) {
        this.appID = appID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    public enum TYPE {
        WEB_CLIENT,
        APPLIACATION
    }

    private String login;

    private byte[] password;

    @Enumerated(EnumType.STRING)
    private TYPE type;

    private UUID appID;

    private String email;

    private String name;

    private String language;

    @ManyToMany
    private List<Role> roles;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public static User getUserByToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        Session s = Ebean.find(Session.class).where().eq("token", token).findUnique();
        if (s == null) {
            return null;
        }
        return getUser(s.getUserID());
    }
    public static User getUser(UUID id) {
        if(id == null) return null;
        return Ebean.find(User.class).where().eq("id", id).findUnique();
    }


}
