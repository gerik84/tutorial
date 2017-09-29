package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by redline on 22.09.17.
 */
@Entity
public class PropertyItem extends ModerateModel {

    private String name;

    @JsonIgnore
    @ManyToOne
    private Property property;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}
