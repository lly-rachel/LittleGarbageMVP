package com.example.littlegarbage.Model.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GarbageData.class},version = 1,exportSchema = false)
public abstract class GarbageDataBase extends RoomDatabase {
    public abstract GarbageDataDao getGarbageDataDao();

}
