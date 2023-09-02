package ru.senya.pixateka.retrofit.itemApi;

import java.io.File;

public class Model {
    String name;
    String author;
    String description;
    File image;


    public Model(String author, File image, String name, String description) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }


}
