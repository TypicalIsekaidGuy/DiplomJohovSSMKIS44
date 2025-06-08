package com.johov.bitcoin.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.johov.bitcoin.data.db.maindata.MainDataDao;
import com.johov.bitcoin.data.db.maindata.MainDataTable;
import com.johov.bitcoin.data.db.transactiondata.TransactionDao;
import com.johov.bitcoin.data.db.transactiondata.TransactionTable;

@Database(entities = {MainDataTable.class, TransactionTable.class}, version =8)//new
public abstract class AppDatabase extends RoomDatabase {
    public abstract MainDataDao mainDataDao();
    public abstract TransactionDao transactionDataDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 6;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app-database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}