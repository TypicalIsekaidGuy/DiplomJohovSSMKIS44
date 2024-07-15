package silmex.apps.airdropcryptopoints.data.networkdata.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import silmex.apps.airdropcryptopoints.data.networkdata.dto.TransactionDTO;

public class TransactionResponse {
    @SerializedName("moneyList")
    public List<TransactionDTO> moneyList;
    @SerializedName("success")
    public int success;
    @SerializedName("message")
    public String message;
}
