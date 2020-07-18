package com.example.littlegarbage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.littlegarbage.bean.DataBaseBean;

import java.util.List;

@Dao
public interface GarbageDataDao {

    @Insert
    long insertGarbageInfo(String garbage,String content);

    @Update
    int updateInfoByGarbage(String garbage,String content);

    @Delete
    int deleteInfoByGarbage(String garbage);

    @Query("SELECT * FROM GARBAGE_TABLE ")
    List<DataBaseBean> queryAllInfo();

    @Query("SELECT * FROM GARBAGE_TABLE ")
    List<String> queryAllGarbageName();


}
