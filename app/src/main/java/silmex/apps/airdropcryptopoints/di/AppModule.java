package silmex.apps.airdropcryptopoints.di;

import android.app.Activity;
import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import silmex.apps.airdropcryptopoints.MainActivity;
import silmex.apps.airdropcryptopoints.data.db.AppDatabase;
import silmex.apps.airdropcryptopoints.data.interfaces.ShowAdsI;
import silmex.apps.airdropcryptopoints.data.repository.MainDataRepository;
import silmex.apps.airdropcryptopoints.data.repository.UnityAdsRepository;
import silmex.apps.airdropcryptopoints.network.RetrofitClient;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public static MainDataRepository providesMainDataRepository(){
        return new MainDataRepository();
    }


    @Singleton
    @Provides
    public static AppDatabase provideAppDatabase(@ApplicationContext Context context){
        return AppDatabase.getDatabase(context);
    }

    @Singleton
    @Provides
    public static Retrofit provideRetrofitClient(){
        return RetrofitClient.getClient();
    }
/*
    @Singleton
    @Provides
    public static UnityAdsRepository provideUnityAdsRepository(@ApplicationContext Context context){
        return new UnityAdsRepository(context);
    }*/

}
