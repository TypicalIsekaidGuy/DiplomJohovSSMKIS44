package silmex.apps.airdropcryptopoints.viewmodel;

import static silmex.apps.airdropcryptopoints.data.repository.MainDataRepository.fullTimerDuration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import silmex.apps.airdropcryptopoints.data.db.AppDatabase;
import silmex.apps.airdropcryptopoints.data.db.maindata.MainDataTable;
import silmex.apps.airdropcryptopoints.data.interfaces.UpgradeWheelCallBack;
import silmex.apps.airdropcryptopoints.data.model.MULTIPLYER_ENUM;
import silmex.apps.airdropcryptopoints.data.networkdata.dto.UserDTO;
import silmex.apps.airdropcryptopoints.data.networkdata.response.BoosterResponse;
import silmex.apps.airdropcryptopoints.data.networkdata.response.UserResponse;
import silmex.apps.airdropcryptopoints.data.repository.MainDataRepository;
import silmex.apps.airdropcryptopoints.data.repository.UnityAdsRepository;
import silmex.apps.airdropcryptopoints.network.MainService;
import silmex.apps.airdropcryptopoints.utils.ConvertUtils;
import silmex.apps.airdropcryptopoints.utils.StringUtils;
import silmex.apps.airdropcryptopoints.utils.TagUtils;

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
    public MutableLiveData<Long> leftTime = new MutableLiveData<Long>(0L);
    public MutableLiveData<Float> progress = new MutableLiveData<Float>(0f);

    @Inject
    HomeViewModel(@ApplicationContext Context context, MainDataRepository mainDataRepository, Retrofit retrofit, AppDatabase db){
        this.mainDataRepository = mainDataRepository;
        this.context = context;
        this.retrofit = retrofit;
        this.db = db;

        unityAdsRepository.initializeUnity();
        setUpObservers();
    }
    private void setUpObservers(){
        mainDataRepository.currentChosenMultipliyer.observeForever(new Observer<MULTIPLYER_ENUM>() {
            @Override
            public void onChanged(MULTIPLYER_ENUM newValue) {

                currentChosenMultipliyer.postValue(newValue);
            }
        });
        mainDataRepository.balance.observeForever(newValue -> balance.postValue(newValue));
        mainDataRepository.isActive.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean newValue) {

                isMining.postValue(newValue);
            }
        });

        mainDataRepository.millisUntilFinishedLiveData.observeForever(new Observer<Long>() {
            @Override
            public void onChanged(Long newValue) {
                if(newValue!=0) {

                    leftTime.setValue(newValue);

                    updateProgress(newValue);

                    if (newValue <= 500L) {
                        onTimerEnd();
                    }
                }
            }
        });
    }

    //presentation functions
    public void onTimerEnd(){
        progress.setValue(0f);
    }

    // main functions
    private void updateProgress(Long estimatedTime){
        progress.setValue( ((float)estimatedTime/fullTimerDuration));
    }

    private void saveUserData(){

        MainService mainService = retrofit.create(MainService.class);

        Call<UserResponse> userResp = mainService.getUser(MainDataRepository.geteDeviceIdentifier());
        userResp.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("network", "PROBLEM");
                    UserResponse userResponse = response.body();
                    UserDTO user = userResponse.users.get(0);

                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        MainDataTable mdt = new MainDataTable(mainDataRepository, ConvertUtils.stringToDate(user.serverTime).getTime());
                        db.mainDataDao().update(mdt);
                        Log.d("Balance",user.serverTime);
                    });
                }
                else {
                    Log.d("network", "failure" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("network", "failure" + t.getMessage());
            }
        });
    }


    //onClick functions
    public void upgradeWheelClick(){
        unityAdsRepository.showUnityAds();
    }
    public void claimClick(){
        mainDataRepository.setTimer();
        mainDataRepository.claimBalance();
        saveUserData();
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
        updateBoostInServer();
        saveUserData();
    }
    private void updateBoostInServer(){
        MainService mainService = retrofit.create(MainService.class);

        Call<BoosterResponse> bosterResp = mainService.updateBooster(MainDataRepository.geteDeviceIdentifier());
        bosterResp.enqueue(new Callback<BoosterResponse>() {
            @Override
            public void onResponse(Call<BoosterResponse> call, Response<BoosterResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("network", "PROBLEM");
                    BoosterResponse bosterResp = response.body();
                    if(bosterResp.success==1){
                        Log.d(TagUtils.MAINVIEWMODELTAG,"Booster upgdraded successfully");
                    }
                }
                else {
                    Log.d("network", "failure" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<BoosterResponse> call, Throwable t) {
                Log.d("network", "failure" + t.getMessage());
            }
        });
    }

    @Override
    public void upgradeWheel() {
        _upgradeWheel();
    }
}
