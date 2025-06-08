package com.johov.bitcoin.data.db.transactiondata;

import static com.johov.bitcoin.data.repository.MainDataRepository.didShowRateWindow;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import com.johov.bitcoin.data.repository.MainDataRepository;
import com.johov.bitcoin.utils.IntegerUtils;
import com.johov.bitcoin.utils.StringUtils;

@Entity(tableName = "transaction_data_table")
public class TransactionTable {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "random_save_id")
    public int id;

    public float bucks;

    public Long creationTime;

    public String promocode;


    public TransactionTable(float bucks){
        this.id = (int) (Math.random()*1000000000);
        this.bucks = bucks;
        this.creationTime = System.currentTimeMillis();
        promocode = StringUtils.generatePromocode();
    }
}
