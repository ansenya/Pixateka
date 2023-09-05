package ru.senya.pixateka.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;


@Entity
public class UserEntity {
    @PrimaryKey
    @NonNull
    private String  id;

    protected String username;

    private String role;

//    private Date created;
//
//    private Date updated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

//    public Date getCreated() {
//        return created;
//    }
//
//    public void setCreated(Date created) {
//        this.created = created;
//    }
//
//    public Date getUpdated() {
//        return updated;
//    }
//
//    public void setUpdated(Date updated) {
//        this.updated = updated;
//    }
}
