package silmex.apps.airdropcryptopoints.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import silmex.apps.airdropcryptopoints.data.networkdata.dto.TransactionDTO;

public class Transaction implements Comparable<Transaction> {
    @SerializedName("creation_time")
    public Date creationTime;
    @SerializedName("value")
    public float value;
    @SerializedName("source")
    public String source;
    @SerializedName("promocode")
    public String promocode;
    @SerializedName("status")
    public int status;

    public Transaction(Date creationTime, float value, String source, String promocode, int status) {
        this.creationTime = creationTime;
        this.value = value;
        this.source = source;
        this.promocode = promocode;
        this.status = status;
    }
    public Transaction(TransactionDTO trans) {
        this.creationTime = trans.creationTime;
        this.value = trans.value;
        this.source = trans.source;
        this.promocode = trans.promocode;
        this.status = trans.status;
    }
    @Override
    public int compareTo(Transaction o) {
        if(this.creationTime==null)
            return -1;
        return this.creationTime.compareTo(o.creationTime);
    }
}