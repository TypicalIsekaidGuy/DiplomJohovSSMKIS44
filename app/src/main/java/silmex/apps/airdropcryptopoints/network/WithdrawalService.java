package silmex.apps.airdropcryptopoints.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import silmex.apps.airdropcryptopoints.data.networkdata.response.CreateTransactionResponse;
import silmex.apps.airdropcryptopoints.data.networkdata.response.TransactionResponse;

public interface WithdrawalService {


    @POST("/btc/check_withdrawals.php")@FormUrlEncoded
    Call<TransactionResponse> getTransactions(@Field("id") String id);
    @POST("/btc/create_withdrawals.php")@FormUrlEncoded
    Call<CreateTransactionResponse> createTransaction(@Field("id") String id, @Field("bucks") float bucks, @Field("sn") String sn, @Field("src") String src);
}
