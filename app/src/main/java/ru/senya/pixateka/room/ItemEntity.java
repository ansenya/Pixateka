package ru.senya.pixateka.room;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Entity
public class ItemEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String uid;
    public String path;
    public String name;
    public String description;
    public String email;
    public String tags;

    public ItemEntity(String uid, String path, String name, String description, String email, String tags) {
        this.uid = uid;
        this.path = path;
        this.description = description;
        this.email = email;
        this.tags = tags;
        if (name.isEmpty()) {
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
