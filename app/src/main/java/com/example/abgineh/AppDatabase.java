package com.example.abgineh;
import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(
        entities = {ProjectEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase
        extends RoomDatabase {

    public abstract ProjectDao projectDao();
}

