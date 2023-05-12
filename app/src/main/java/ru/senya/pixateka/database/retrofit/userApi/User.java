package ru.senya.pixateka.database.retrofit.userApi;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int id;
    public String username;
    public String avatar;
    public String email;
    public String first_name;
    public String last_name;
    public String country;
    public String token;
    public String sessionId;


    public User(int id, String username, String avatar, String email, String first_name, String last_name, String country) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.country = country;
    }



    @Ignore
    public User(int id, String username, String avatar, String email, String first_name, String last_name, String country, String token, String sessionId) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.country = country;
        this.token = token;
        this.sessionId = sessionId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
