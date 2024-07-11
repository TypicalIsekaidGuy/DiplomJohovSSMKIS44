package silmex.apps.airdropcryptopoints.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import silmex.apps.airdropcryptopoints.data.networkdata.response.LogResponse;

public interface LogService {
    @POST("/airdrop/server_logs.php")@FormUrlEncoded
    Call<LogResponse> log(@Field("user_id") String user_id, @Field("log") String log, @Field("app") String app);

}
