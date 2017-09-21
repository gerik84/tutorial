package models;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ebean.Ebean;
import io.ebean.Model;
import utils.Auth;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by pavel on 21.09.17.
 */
@MappedSuperclass
abstract public class BaseModel extends Model {

    @Id
    private UUID id;
    private Long whenCreated;
    private Long whenUpdated;
    private Long whenDeleted = null;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    //functions for overwriting in children classes
    public boolean checkMinimumProperties() {
        return true;
    }

    public boolean checkUniqueFields() {
        return true;
    }

    public Object preModify(){return null;}

    public HashSet<String> excludedMethods() {
        return null;
    }
    public void beforeUpdate(ActorSystem actorSystem, Object pre) {

    }
    public void postModify(ActorSystem actorSystem, Object pre) {

    }
    public void m_delete() {
        setWhenDeleted(System.currentTimeMillis());
        update();
    }
    //end



    public Long getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Long whenCreated) {
        this.whenCreated = whenCreated;
    }

    public Long getWhenUpdated() {
        return whenUpdated;
    }

    public void setWhenUpdated(Long whenUpdated) {
        this.whenUpdated = whenUpdated;
    }

    @JsonProperty
    public Long getWhenDeleted()
    {
        return whenDeleted;
    }

    public void setWhenDeleted(Long whenDeleted)
    {
        this.whenDeleted = whenDeleted;
    }

    @Override
    public void update() {
        whenUpdated = System.currentTimeMillis();
        super.update();
    }



    public void updateWithoutModify() {
        super.update();
    }


    @Override
    public void save() {
        if (whenUpdated == null) {
            whenUpdated = System.currentTimeMillis();
        }
        if(whenCreated == null) {
            whenCreated = System.currentTimeMillis();
        }
        super.save();
    }

    @Override
    public void update(String server) {
        whenUpdated = System.currentTimeMillis();
        super.update(server);
    }

    public void postCreation() {
    }


    public static <T> T getByID(Class<T> model, UUID id) {
        if(id == null) return null;
        return Ebean.find(model).where().eq("id", id).findUnique();
    }

    public static <T> T getByID(Class<T> model, String id) {
        if(id == null) return null;
        return getByID(model, UUID.fromString(id));
    }


}
