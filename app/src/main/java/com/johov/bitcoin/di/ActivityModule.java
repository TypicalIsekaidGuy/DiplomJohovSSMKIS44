package com.johov.bitcoin.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.components.ActivityComponent;
import com.johov.bitcoin.data.repository.UnityAdsRepository;

@Module
@InstallIn(ActivityComponent.class)
public class ActivityModule {

    @Provides
    public static UnityAdsRepository provideUnityAdsRepository(@ActivityContext Context context){
        return new UnityAdsRepository(context);
    }
}
