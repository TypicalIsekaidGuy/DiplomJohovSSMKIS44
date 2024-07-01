package silmex.apps.airdropcryptopoints.data.model;

import com.google.gson.annotations.SerializedName;

public class Transaction {
    @SerializedName("creation_time")
    public String creationTime;
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

    public Transaction(String creationTime, float value, String serverTime, String source, String email, String promocode, int status) {
        this.creationTime = creationTime;
        this.value = value;
        this.serverTime = serverTime;
        this.source = source;
        this.email = email;
        this.promocode = promocode;
        this.status = status;
    }
/*    public Transaction(TransactionTable trans){
        this.creationTime = trans.creationTime;
        this.value = trans.value;
        this.serverTime = trans.serverTime;
        this.source = trans.source;
        this.email = trans.email;
        this.promocode = trans.promocode;
        this.status = trans.status;
    }*/
}