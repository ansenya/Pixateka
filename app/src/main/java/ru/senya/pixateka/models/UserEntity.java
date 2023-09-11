package ru.senya.pixateka.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;


@Entity
public class UserEntity {
    @PrimaryKey
    @NonNull
    private String id;

    protected String username;

    protected String name;
    protected String bio;


    private String pfp;
    private String back;

    private String role;

//    private Date created;
//
//    private Date updated;


    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPfp() {
        return pfp;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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
