package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;

/**
 * Created by redline on 22.09.17.
 */
@Entity
public class Property extends ModerateModel {

    private String name;

    private boolean multiChoose = false;

    @OneToMany
    private List<PropertyItem> items;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMultiChoose() {
        return multiChoose;
    }

    public void setMultiChoose(boolean multiChoose) {
        this.multiChoose = multiChoose;
    }

    public List<PropertyItem> getItems() {
        return items;
    }

    public void setItems(List<PropertyItem> items) {
        this.items = items;
    }
}
