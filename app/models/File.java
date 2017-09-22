package models;


import io.ebean.Ebean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.UUID;


@Entity
public class File extends BaseModel
{

    @Id
    private UUID id;
    private String type;
    private int length;
    @Lob
    private byte[] data;
    @Lob
    private byte[] thumbnail;

    public static File getFile(UUID id) {
        return Ebean.find(File.class).where().eq("id", id).findUnique();
    }

    public static File getFile(String id) {
        return getFile(UUID.fromString(id));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

}
