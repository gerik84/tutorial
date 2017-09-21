package models;

import io.ebean.Ebean;
import io.ebean.Model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;

/**
 * Created by pavel on 21.09.17.
 */
@Entity
public class Role extends Model {

    public enum ROLE {
        edit, add, deleted, view
    }

    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    @Id
    @Enumerated(EnumType.STRING)
    private ROLE role;

    public static List<Role> getRoles(HashSet<ROLE> roles) {
        return Ebean.find(Role.class).where().in("role", roles.toArray()).findList();
    }



}
