package ru.senya.pixateka.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int pfp;
    public int back;
    public String name;
    public String surname;
    public String geo;
    public String birthday;
    public String about;

    public UserEntity(int pfp, int back, String name, String surname,
                      String geo, String birthday, String about) {
        this.pfp = pfp;
        this.back = back;
        this.name = name;
        this.surname = surname;
        this.geo = geo;
        this.birthday = birthday;
        this.about = about;
    }
}
