package com.johov.bitcoin.data.db.transactiondata;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.johov.bitcoin.data.db.maindata.MainDataTable;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    abstract void insert(com.johov.bitcoin.data.db.transactiondata.TransactionTable data);
    @Update
    abstract void update(com.johov.bitcoin.data.db.transactiondata.TransactionTable data);


    @Query("SELECT * FROM transaction_data_table")
    abstract List<TransactionTable> getAll();
}
