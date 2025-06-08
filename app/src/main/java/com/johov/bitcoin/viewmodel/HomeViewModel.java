package com.johov.bitcoin.viewmodel;

import static com.johov.bitcoin.data.repository.MainDataRepository.fullTimerDuration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Retrofit;
import com.johov.bitcoin.MainActivity;
import com.johov.bitcoin.data.db.AppDatabase;
import com.johov.bitcoin.data.db.maindata.MainDataTable;
import com.johov.bitcoin.data.interfaces.UpgradeWheelCallBack;
import com.johov.bitcoin.data.model.enums.CONNECTION_ERROR_ENUM;
import com.johov.bitcoin.data.model.enums.MULTIPLYER_ENUM;
import com.johov.bitcoin.data.repository.MainDataRepository;
import com.johov.bitcoin.data.repository.UnityAdsRepository;
import com.johov.bitcoin.utils.ConvertUtils;
import com.johov.bitcoin.utils.StringUtils;
import com.johov.bitcoin.utils.TagUtils;

@HiltViewModel
public class HomeViewModel extends ViewModel implements UpgradeWheelCallBack {
    public MainDataRepository mainDataRepository;
    Context context;

    Retrofit retrofit;

    AppDatabase db;

    @SuppressLint("StaticFieldLeak")
    public static UnityAdsRepository unityAdsRepository;

    //main vars
    public MutableLiveData<MULTIPLYER_ENUM> currentChosenMultipliyer = new MutableLiveData<>(MULTIPLYER_ENUM.MULTYPLIER_1x);
    public MutableLiveData<Float> balance = new MutableLiveData<>(0f);
    public MutableLiveData<Boolean> isMining = new MutableLiveData<>(false);

    //presentation vars
    public MutableLiveData<Long> leftTime = new MutableLiveData<>(0L);
    public MutableLiveData<Float> progress = new MutableLiveData<>(0f);

    @Inject
    HomeViewModel(@ApplicationContext Context context, MainDataRepository mainDataRepository, AppDatabase db){
        this.mainDataRepository = mainDataRepository;
        this.context = context;
        this.retrofit = retrofit;
        this.db = db;

        initializeUnity();
        setUpObservers();
    }

    //main functions
    private void setUpObservers(){
        mainDataRepository.currentChosenMultipliyer.observeForever(newValue -> currentChosenMultipliyer.postValue(newValue));

        mainDataRepository.balance.observeForever(newValue -> balance.postValue(newValue));

        mainDataRepository.isActive.observeForever(newValue -> isMining.postValue(newValue));

        mainDataRepository.millisUntilFinishedLiveData.observeForever(newValue -> {
            if(newValue!=0) {

                leftTime.setValue(newValue);

                updateProgress(newValue);

                if (newValue <= 500L) {
                    onTimerEnd();
                }
            }
        });
    }

    public void initializeUnity(){
        if(isOnline()){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() ->{
                Log.d("NETWORKTAG2",""+MainDataRepository.unityID);
                Log.d("NETWORKTAG2",""+MainDataRepository.unityBlock);
                unityAdsRepository.initializeUnity();
            }, 1000);

        }
        else{
            MainViewModel.throwConnectionError(CONNECTION_ERROR_ENUM.UNITY_INITIALIZATION);
        }
    }

    private void saveUserData(){

        AppDatabase.databaseWriteExecutor.execute(() -> {
            MainDataTable mdt = new MainDataTable(mainDataRepository, System.currentTimeMillis());
            db.mainDataDao().update(mdt);
            Log.d("Balance",""+System.currentTimeMillis());
        });
    }

    //presentation functions
    public void onTimerEnd(){
        progress.setValue(0f);
    }
    private void updateProgress(Long estimatedTime){
        progress.setValue( ((float)estimatedTime/fullTimerDuration));
    }

    //onClick functions
    public void upgradeWheelClick(){
        if (isOnline()) {
            unityAdsRepository.showUnityAds();
        } else {
            MainViewModel.throwConnectionError(CONNECTION_ERROR_ENUM.UPGRADE_WHEEL_CLICK);
        }
    }
    public void claimClick(){
        if(isOnline()){
            mainDataRepository.claimBalance();
            mainDataRepository.setTimer();
            saveUserData();
        }
        else{
            MainViewModel.throwConnectionError(CONNECTION_ERROR_ENUM.CLAIM_CLICK);
        }
    }

    //helper functions
    private   void _upgradeWheel(){
        Log.d(TagUtils.MAINVIEWMODELTAG,"callback called");

        switch (Objects.requireNonNull(currentChosenMultipliyer.getValue())){
            case MULTYPLIER_1x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_2x);

                MainViewModel.log("User with id"+ StringUtils.generateDeviceIdentifier()+" updated boost to x2");
            }
            break;
            case MULTYPLIER_2x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_3x);

                MainViewModel.log("User with id"+ StringUtils.generateDeviceIdentifier()+" updated boost to x3");
            }
            break;
            case MULTYPLIER_3x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_5x);

                MainViewModel.log("User with id"+ StringUtils.generateDeviceIdentifier()+" updated boost to x5");
            }
            break;
            case MULTYPLIER_5x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_8x);

                MainViewModel.log("User with id"+ StringUtils.generateDeviceIdentifier()+" updated boost to x8");
            }
            break;
            case MULTYPLIER_8x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_13x);

                MainViewModel.log("User with id"+ StringUtils.generateDeviceIdentifier()+" updated boost to x13");
            }
            break;
            case MULTYPLIER_13x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_21x);

                MainViewModel.log("User with id"+ StringUtils.generateDeviceIdentifier()+" updated boost to x21");
            }
            break;
            case MULTYPLIER_21x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_34x);

                MainViewModel.log("User with id"+ StringUtils.generateDeviceIdentifier()+" updated boost to x34");
            }
            break;
            case MULTYPLIER_34x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_55x);

                MainViewModel.log("User with id"+ StringUtils.generateDeviceIdentifier()+" updated boost to x55");
            }
            break;
            case MULTYPLIER_55x:{

            }
            break;
        }
        saveUserData();
    }

    @Override
    public void upgradeWheel() {
        _upgradeWheel();
    }

    //util functions
    public boolean isOnline(){
        return MainActivity.Companion.isOnline((Context) context);
    }
}
