package com.johov.bitcoin.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import com.johov.bitcoin.data.db.AppDatabase;
import com.johov.bitcoin.data.repository.MainDataRepository;

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

}
