package ru.senya.pixateka.subjects;

import android.net.Uri;

public class Item {

    private int pic;
    private Uri uri;
    private String name;


    public Item(int pic, String name) {
        this.pic = pic;
        if (name.isEmpty()){
            this.name = "no text";
            return;
        } else this.name = name;
    }

    public Item(Uri uri, String name) {
        this.uri = uri;
        if (name.isEmpty()){
            this.name = "no text";
            return;
        } else this.name = name;

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
