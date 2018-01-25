package me.arminb.sara.entities;


import java.util.Date;

public abstract class HistoricalBaseEntity extends BaseEntity{

    protected Date modifiedAt;

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void setModifiedAtToNow(){
        modifiedAt = new Date();
    }
}
