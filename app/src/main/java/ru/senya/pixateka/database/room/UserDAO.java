package ru.senya.pixateka.database.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import ru.senya.pixateka.models.UserEntity;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM UserEntity")
    UserEntity[] getUser();

    @Insert
    void save(UserEntity userEntity);

    @Query("DELETE FROM UserEntity")
    void deleteUserTable();
}
