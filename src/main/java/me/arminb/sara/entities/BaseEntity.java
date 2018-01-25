package me.arminb.sara.entities;

import org.bson.types.ObjectId;

import java.util.Date;

public abstract class BaseEntity {

    protected String id;
    protected Date createdAt;
    protected Date modifiedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void changeModifiedDate(){
        modifiedAt = new Date();
    }

    public void setCreationDate(){
        createdAt = new ObjectId(id).getDate();
    }
}
