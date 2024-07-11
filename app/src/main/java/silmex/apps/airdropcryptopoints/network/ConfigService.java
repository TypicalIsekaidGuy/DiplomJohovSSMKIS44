package silmex.apps.airdropcryptopoints.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import silmex.apps.airdropcryptopoints.data.networkdata.dto.ConfigDTO;

public interface ConfigService {

    @GET("/airdrop/config-default.json")
    Call<ConfigDTO> getConfigDefault();
    @GET("/airdrop/config-{lang}.json")
    Call<ConfigDTO> getConfig(@Path("lang") String lang);

}
