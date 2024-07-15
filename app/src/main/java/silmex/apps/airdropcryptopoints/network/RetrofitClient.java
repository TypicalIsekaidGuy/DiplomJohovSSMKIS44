package silmex.apps.airdropcryptopoints.network;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    static GsonBuilder gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create().newBuilder();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://servers-big-data.ru/")
                    .addConverterFactory(GsonConverterFactory.create(gson.create()))
                    .build();
        }
        return retrofit;
    }
}