package silmex.apps.airdropcryptopoints.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import silmex.apps.airdropcryptopoints.data.networkdata.response.BoosterResponse;
import silmex.apps.airdropcryptopoints.data.networkdata.response.ReferalsResponse;
import silmex.apps.airdropcryptopoints.data.networkdata.response.UserResponse;

public interface MainService {

    @POST("/airdrop/get_user.php")@FormUrlEncoded
    Call<UserResponse> getUser(@Field("id") String id);
    @POST("/airdrop/update_booster.php")@FormUrlEncoded
    Call<BoosterResponse> updateBooster(@Field("id") String id);
    @POST("/airdrop/update_referals.php")@FormUrlEncoded
    Call<ReferalsResponse> updateReferals(@Field("id") String id, @Field("entered_code") String enteredCode);

}
