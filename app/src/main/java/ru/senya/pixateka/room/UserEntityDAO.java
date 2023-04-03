package ru.senya.pixateka.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserEntityDAO {
    @Query("SELECT * FROM UserItemEntity")
    List<UserItemEntity> getAll();

    @Insert
    void save(UserItemEntity item);

    @Query("SELECT * FROM UserItemEntity WHERE id = :id")
    UserItemEntity getId(int id);

    @Query("SELECT * FROM UserItemEntity WHERE name = :name")
    UserItemEntity get(String name);

    @Query("DELETE FROM UserItemEntity WHERE id = :id")
    void deleteByUserId(long id);
    @Query("DELETE FROM UserItemEntity")
    void delete();
}
