package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pavel on 21.11.17.
 */
@Entity
public class OrderItem extends UUIDBaseModel {

    @ManyToOne
    private Goods goods;

    @ManyToOne
    @JsonIgnore
    private Order order;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Comment> comments;

    @Enumerated(EnumType.STRING)
    private Order.STATUS status = Order.STATUS.none;

    private Integer amount;

    private Long byDate = null;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Order.STATUS getStatus() {
        return status;
    }

    public void setStatus(Order.STATUS status) {
        this.status = status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getByDate() {
        return byDate;
    }

    public void setByDate(Long byDate) {
        this.byDate = byDate;
    }
}
