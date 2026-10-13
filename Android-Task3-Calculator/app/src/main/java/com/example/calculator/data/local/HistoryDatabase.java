package com.example.calculator.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {HistoryEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class HistoryDatabase extends RoomDatabase {

    public abstract HistoryDao historyDao();

    private static volatile HistoryDatabase INSTANCE;

    public static HistoryDatabase getInstance(Context context) {

        if (INSTANCE == null) {

            synchronized (HistoryDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    HistoryDatabase.class,
                                    "calculator_database"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}