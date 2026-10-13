package com.example.calculator.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.calculator.data.local.HistoryDao;
import com.example.calculator.data.local.HistoryDatabase;
import com.example.calculator.data.local.HistoryEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryRepository {

    private final HistoryDao historyDao;

    private final LiveData<List<HistoryEntity>> allHistory;

    private final ExecutorService databaseExecutor;

    public HistoryRepository(Application application) {

        HistoryDatabase database =
                HistoryDatabase.getInstance(application);

        historyDao = database.historyDao();

        allHistory = historyDao.getAllHistory();

        databaseExecutor =
                Executors.newSingleThreadExecutor();
    }


    // GET HISTORY


    public LiveData<List<HistoryEntity>> getAllHistory() {

        return allHistory;
    }


    // INSERT HISTORY


    public void insert(
            String expression,
            String result
    ) {

        databaseExecutor.execute(() -> {

            HistoryEntity history =
                    new HistoryEntity(
                            expression,
                            result,
                            System.currentTimeMillis()
                    );

            historyDao.insert(history);
        });
    }


    // DELETE ONE


    public void delete(HistoryEntity history) {

        databaseExecutor.execute(() -> {

            historyDao.delete(history);
        });
    }


    // DELETE ALL


    public void deleteAll() {

        databaseExecutor.execute(() -> {

            historyDao.deleteAll();
        });
    }


    // SHUTDOWN


    public void shutdown() {

        databaseExecutor.shutdown();
    }
}