package silmex.apps.airdropcryptopoints.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Transaction implements Comparable<Transaction> {
    @SerializedName("creation_time")
    public Date creationTime;
    @SerializedName("value")
    public float value;
    @SerializedName("server_time")
    public String serverTime;
    @SerializedName("source")
    public String source;
    @SerializedName("email")
    public String email;
    @SerializedName("promocode")
    public String promocode;
    @SerializedName("status")
    public int status;

    public Transaction(Date creationTime, float value, String serverTime, String source, String email, String promocode, int status) {
        this.creationTime = creationTime;
        this.value = value;
        this.serverTime = serverTime;
        this.source = source;
        this.email = email;
        this.promocode = promocode;
        this.status = status;
    }
    @Override
    public int compareTo(Transaction o) {
        if(this.creationTime==null)
            return -1;
        return this.creationTime.compareTo(o.creationTime);
    }
}