package ru.senya.pixateka.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM UserEntity")
    List<UserEntity> getAll();

    @Insert
    void save(UserEntity item);

    @Query("SELECT * FROM UserEntity WHERE id = :id")
    UserEntity getId(int id);

    @Query("SELECT * FROM UserEntity WHERE name = :name")
    UserEntity get(String name);

    @Query("DELETE FROM UserEntity WHERE id = :id")
    void deleteByUserId(long id);
    @Query("DELETE FROM UserEntity")
    void delete();
}
