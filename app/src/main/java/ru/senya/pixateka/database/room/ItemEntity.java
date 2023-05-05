package ru.senya.pixateka.database.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class ItemEntity {

    @PrimaryKey
    public int id;
    public String uid;
    public String path;
    public String name;
    public String description;
    public String email;
    public String tags;

    public ItemEntity(int id, String uid, String path, String name, String description, String email, String tags) {
        this.id = id;
        this.uid = uid;
        this.path = path;
        this.description = description;
        this.email = email;
        this.tags = tags;
        if (name==null || name.isEmpty()) {
            this.name = "no text";
            return;
        } else this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTags() {
        return tags;
    }

    public String getEmail() {
        return email;
    }
}
