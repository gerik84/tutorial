package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pavel on 21.11.17.
 */
@Entity
@Table(name = "orders")
public class Order extends UUIDBaseModel {

    @ManyToOne
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    private STATUS status;

    private Long price;

    @ManyToMany
    private List<Comment> comments;

    @ManyToMany
    @OrderBy("whenCreated DESC")
    private List<Log> logs;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public enum STATUS {
        none,
        deleted,
        pending,
        done,
        decline,
        suspended,
        archive;
    }

}
