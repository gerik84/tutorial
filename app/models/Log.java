package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.UUID;

/**
 * Created by pavel on 21.11.17.
 */
@Entity
public class Log extends UUIDBaseModel {

    @ManyToOne
    private User user;

    private UUID subjectID;

    private String oldValue;

    private String newValue;

    private MODE mode;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(UUID subjectID) {
        this.subjectID = subjectID;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public MODE getMode() {
        return mode;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
    }

    public enum MODE {
        order,
        goods
    }

}
