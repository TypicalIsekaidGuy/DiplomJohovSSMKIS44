package silmex.apps.airdropcryptopoints.data.networkdata.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import silmex.apps.airdropcryptopoints.data.networkdata.dto.UserDTO;

public class UserResponse {
    @SerializedName("users")
    public List<UserDTO> users;

    @SerializedName("success")
    public int success;

    @SerializedName("message")
    public String message;
}