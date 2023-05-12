package ru.senya.pixateka.database.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDAO {

    @Query("SELECT * FROM ItemEntity")
    List<ItemEntity> getAll();
    @Query("SELECT * FROM ItemEntity WHERE id=:id")
    List<ItemEntity> getAllProfile(int id);

    @Insert
    void save(ItemEntity item);

    @Query("SELECT * FROM ItemEntity WHERE id = :id")
    ItemEntity getId(int id);

    @Query("DELETE FROM ItemEntity WHERE id = :id")
    void deleteByUserId(long id);
    @Query("DELETE FROM ItemEntity")
    void delete();

    @Query("SELECT * FROM ItemEntity WHERE tags =:tags AND tags!='' AND id!=:id")
    List<ItemEntity> searchByTags(String tags, String id);

}
