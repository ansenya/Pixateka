package ru.senya.pixateka.database.retrofit.userApi.old;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class UserOld {
    @PrimaryKey
    public int id;
    public String username;
    public String avatar;
    public String background;
    public String email;
    public String first_name;
    public String last_name;
    public String country;
    public String token;
    public String sessionId;
    public String about;



    public UserOld(int id, String username, String avatar, String email, String first_name, String last_name, String country) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.country = country;
    }



    @Ignore
    public UserOld(int id, String username, String avatar, String email, String first_name, String last_name, String country, String token, String sessionId, String about, String background) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.country = country;
        this.token = token;
        this.sessionId = sessionId;
        this.about = about;
        this.background = background;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
