package me.arminb.sara.entities;

import org.bson.types.ObjectId;

import java.util.Date;

public abstract class BaseEntity {

    protected String id;
    protected Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        createdAt = new ObjectId(id).getDate();
    }


}
