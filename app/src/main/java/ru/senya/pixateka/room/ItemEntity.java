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

//    @TypeConverter({Converter.class})
//    public Uri uri;
    private String name;




    public ItemEntity(int pic, String name) {
        this.pic = pic;
        if (name.isEmpty()){
            this.name = "no text";
            return;
        } else this.name = name;
    }

    @Ignore
    public ItemEntity(Uri uri, String name) {
//        this.uri = uri;
//        if (name.isEmpty()){
//            this.name = "no text";
//            return;
//        } else this.name = name;

    }


    public int getId() {
        return id;
    }
    public int getPic() {
        return pic;
    }

    public Uri getUri() {
//        return uri;
        return null;
    }

    public String getName() {
        return name;
    }

    public class Converter{

        @TypeConverter
        public String fromUri(Uri uri){
            return uri.toString();
        }

        @TypeConverter
        public Uri toUri(String s){
            return Uri.parse(s);
        }
    }


}
