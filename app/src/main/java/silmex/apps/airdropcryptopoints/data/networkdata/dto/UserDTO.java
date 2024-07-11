package silmex.apps.airdropcryptopoints.data.networkdata.dto;

import com.google.gson.annotations.SerializedName;

public class UserDTO {
    @SerializedName("ref_code")
    public int refCode;

    @SerializedName("server_time")
    public String serverTime;

    @SerializedName("id")
    public String id;

    @SerializedName("boost_timer")
    public String boostTimer;

    @SerializedName("referals")
    public int referals;

    @SerializedName("entered_code")
    public String enteredCode;
}

