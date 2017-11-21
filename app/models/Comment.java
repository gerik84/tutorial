package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by pavel on 21.11.17.
 */
@Entity
public class Comment extends UUIDBaseModel {

    @ManyToOne
    private User user;

    private String message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
