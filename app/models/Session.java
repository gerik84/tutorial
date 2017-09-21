package models;

import io.ebean.Ebean;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

/**
 * Created by pavel on 21.09.17.
 */
@Entity
public class Session extends BaseModel {

    private UUID user_id;

    @Column(length = 1000, unique = true)
    private String token;

    public static Session makeSession(String token, User user) {
        Session s = Ebean.find(Session.class).where().eq("token", token).findUnique();
        if (s != null) {
            return s;
        }
        s = new Session();
        s.setToken(token);
        s.setUserId(user.getId());
        s.save();
        return s;
    }

    public UUID getUserID() {
        return user_id;
    }

    public void setUserId(UUID user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
