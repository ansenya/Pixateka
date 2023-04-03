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
    public int pic;
    public String path;
    public String category;
    public String tags;
    private String name;

    public String getCategory() {
        return category;
    }

    public String getTags() {
        return tags;
    }

    public ItemEntity(int pic, String name) {
        this.pic = pic;
        if (name.isEmpty()){
            this.name = "no text";
            return;
        } else this.name = name;
    }

    @Ignore
    public ItemEntity(String path, String name, String category, String tags) {
        this.path = path;
        this.category = category;
        this.tags=tags;
        if (name.isEmpty()){
            this.name = "no text";
            return;
        } else this.name = name;
    }


    public int getId() {
        return id;
    }
    public int getPic() {
        return pic;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }



}
