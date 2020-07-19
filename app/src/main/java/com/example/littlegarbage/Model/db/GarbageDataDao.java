package com.example.littlegarbage.Model.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface GarbageDataDao {

    @Insert
    long insertGarbageInfo(GarbageData garbageData);

    @Query("UPDATE GARBAGE_TABLE SET content =:content WHERE garbage =:garbage")
    int updateInfoByGarbage(String garbage,String content);

    @Query("DELETE FROM GARBAGE_TABLE WHERE garbage = :garbage")
    int deleteInfoByGarbage(String garbage);

//    @Query("SELECT * FROM GARBAGE_TABLE ORDER BY ID ASC")
//    List<GarbageDataBase> queryAllInfo();

    @Query("SELECT garbage FROM GARBAGE_TABLE ORDER BY ID ASC")
    List<String> queryAllGarbageName();


}
