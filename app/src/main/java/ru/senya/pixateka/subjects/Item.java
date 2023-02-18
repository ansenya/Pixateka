package ru.senya.pixateka.subjects;

public class Item {

    private int pic;
    private String name;


    public Item(int pic, String name) {
        this.pic = pic;
        this.name = name;
    }

    public int getPic() {
        return pic;
    }

    public String getName() {
        return name;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
