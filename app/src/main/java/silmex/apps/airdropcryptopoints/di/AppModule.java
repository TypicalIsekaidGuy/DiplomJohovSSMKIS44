package silmex.apps.airdropcryptopoints.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import silmex.apps.airdropcryptopoints.data.repository.MainDataRepository;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Provides
    @Singleton
    public static MainDataRepository providesMainDataRepository(){
        return new MainDataRepository();
    }
}
