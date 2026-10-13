package com.example.calculator.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    void insert(HistoryEntity history);

    @Query("SELECT * FROM calculation_history ORDER BY timestamp DESC")
    LiveData<List<HistoryEntity>> getAllHistory();

    @Delete
    void delete(HistoryEntity history);

    @Query("DELETE FROM calculation_history")
    void deleteAll();
}