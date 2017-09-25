package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

/**
 * Created by redline on 22.09.17.
 */
@Entity
public class Brand extends ModerateModel {

    @Column(unique = true)
    private String name;

    private String description;

    @OneToOne
    private Media icon;

}
