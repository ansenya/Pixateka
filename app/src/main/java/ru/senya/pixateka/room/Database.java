package ru.senya.pixateka.room;


import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationSpec;

import java.util.Set;

@androidx.room.Database(entities = {ItemEntity.class, UserEntity.class, UserItemEntity.class}, version = 3)
public abstract class Database extends RoomDatabase {
    public abstract ItemDAO itemDAO();
    public abstract UserDAO userDAO();
    public abstract UserEntityDAO userEntityDAO();
}
