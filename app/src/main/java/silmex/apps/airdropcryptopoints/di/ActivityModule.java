package silmex.apps.airdropcryptopoints.di;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;
import silmex.apps.airdropcryptopoints.data.interfaces.CallbackI;
import silmex.apps.airdropcryptopoints.data.repository.UnityAdsRepository;
import silmex.apps.airdropcryptopoints.viewmodel.HomeViewModel;

@Module
@InstallIn(ActivityComponent.class)
public class ActivityModule {

    @Provides
    public static UnityAdsRepository provideUnityAdsRepository(@ActivityContext Context context){
        return new UnityAdsRepository(context);
    }
}
