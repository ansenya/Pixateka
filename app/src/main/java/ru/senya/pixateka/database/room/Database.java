package ru.senya.pixateka.database.room;


import androidx.room.RoomDatabase;


@androidx.room.Database(entities = {ItemEntity.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract ItemDAO itemDAO();
}
