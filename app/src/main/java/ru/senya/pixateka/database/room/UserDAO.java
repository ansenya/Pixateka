package ru.senya.pixateka.database.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import ru.senya.pixateka.database.retrofit.userApi.User;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM User")
    User[] getUser();

    @Insert
    void save(User user);

    @Query("DELETE FROM User")
    void deleteUserTable();
}
