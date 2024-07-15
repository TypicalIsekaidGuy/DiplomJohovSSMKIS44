package silmex.apps.airdropcryptopoints.data.networkdata.response;

import com.google.gson.annotations.SerializedName;

public class CreateTransactionResponse {
    @SerializedName("success")
    public int success;
    @SerializedName("message")
    public String message;
    @SerializedName("promocode")
    public String promocode;
}

