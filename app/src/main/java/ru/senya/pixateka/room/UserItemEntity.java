package ru.senya.pixateka.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserItemEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String path;
    public String category;
    public String tags;
    public String name;

    public UserItemEntity(String path, String name) {
        this.path = path;
        this.name = name;
    }
}
