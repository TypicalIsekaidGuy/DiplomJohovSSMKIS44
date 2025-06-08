package com.johov.bitcoin.data.model;

import com.google.gson.annotations.SerializedName;
import com.johov.bitcoin.data.db.transactiondata.TransactionTable;

import java.util.Date;

public class Transaction implements Comparable<Transaction> {
    @SerializedName("creation_time")
    public Date creationTime;
    @SerializedName("value")
    public float value;
    @SerializedName("promocode")
    public String promocode;
    @SerializedName("status")
    public int status;

    public Transaction(Date creationTime, float value, String promocode, int status) {
        this.creationTime = creationTime;
        this.value = value;
        this.promocode = promocode;
        this.status = status;
    }

    public Transaction(TransactionTable transactionTable) {
        this.creationTime = new Date(transactionTable.creationTime);
        this.value = transactionTable.bucks;
        this.promocode = transactionTable.promocode;
        this.status = 2;
    }

    @Override
    public int compareTo(Transaction o) {
        if(this.creationTime==null)
            return -1;
        return this.creationTime.compareTo(o.creationTime);
    }
}