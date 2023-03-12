package ru.senya.pixateka.subjects;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class Item {

    private int pic;
    private Uri uri;
    private String name;


    public Item(int pic, String name) {
        this.pic = pic;
        this.name = name;
    }

    public Item(Uri uri, String name) {
        this.name = name;
        this.uri = uri;
    }

    public int getPic() {
        return pic;
    }

    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
