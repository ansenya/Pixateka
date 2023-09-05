package ru.senya.pixateka.database.room;


import androidx.room.RoomDatabase;

import ru.senya.pixateka.models.UserEntity;


@androidx.room.Database(entities = {ItemEntity.class, UserEntity.class}, version = 2)
public abstract class Database extends RoomDatabase {
    public abstract ItemDAO itemDAO();

    public abstract UserDAO userDAO();
}
