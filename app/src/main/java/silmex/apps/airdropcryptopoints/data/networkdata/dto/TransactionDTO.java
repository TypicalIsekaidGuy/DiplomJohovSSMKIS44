package silmex.apps.airdropcryptopoints.data.networkdata.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import silmex.apps.airdropcryptopoints.data.model.Transaction;


public class TransactionDTO{
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

    public TransactionDTO(Date creationTime, float value, String serverTime, String source, String email, String promocode, int status) {
        this.creationTime = creationTime;
        this.value = value;
        this.serverTime = serverTime;
        this.source = source;
        this.email = email;
        this.promocode = promocode;
        this.status = status;
    }
}