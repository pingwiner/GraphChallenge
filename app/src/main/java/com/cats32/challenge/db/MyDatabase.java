package com.cats32.challenge.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                DbPoint.class
        },
        version = 1,
        exportSchema = false
)
public abstract class MyDatabase extends RoomDatabase {
   public abstract PointsDao pointsDao();
}

