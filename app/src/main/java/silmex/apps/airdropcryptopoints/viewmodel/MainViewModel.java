package silmex.apps.airdropcryptopoints.viewmodel;

import static silmex.apps.airdropcryptopoints.data.repository.MainDataRepository.fullTimerDuration;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import silmex.apps.airdropcryptopoints.MainActivity;
import silmex.apps.airdropcryptopoints.data.db.AppDatabase;
import silmex.apps.airdropcryptopoints.data.db.maindata.MainDataDao;
import silmex.apps.airdropcryptopoints.data.db.maindata.MainDataTable;
import silmex.apps.airdropcryptopoints.data.model.enums.CONNECTION_ERROR_ENUM;
import silmex.apps.airdropcryptopoints.data.model.enums.MULTIPLYER_ENUM;
import silmex.apps.airdropcryptopoints.data.networkdata.dto.ConfigDTO;
import silmex.apps.airdropcryptopoints.data.networkdata.dto.UserDTO;
import silmex.apps.airdropcryptopoints.data.networkdata.response.LogResponse;
import silmex.apps.airdropcryptopoints.data.networkdata.response.UserResponse;
import silmex.apps.airdropcryptopoints.data.repository.MainDataRepository;
import silmex.apps.airdropcryptopoints.network.ConfigService;
import silmex.apps.airdropcryptopoints.network.LogService;
import silmex.apps.airdropcryptopoints.network.MainService;
import silmex.apps.airdropcryptopoints.network.RetrofitClient;
import silmex.apps.airdropcryptopoints.ui.view.composables.Coin;
import silmex.apps.airdropcryptopoints.utils.ConvertUtils;
import silmex.apps.airdropcryptopoints.utils.IntegerUtils;
import silmex.apps.airdropcryptopoints.utils.MethodUtils;
import silmex.apps.airdropcryptopoints.utils.StringUtils;
import silmex.apps.airdropcryptopoints.utils.TagUtils;

@HiltViewModel
public class MainViewModel extends ViewModel {
    public MainDataRepository mainDataRepository;

    @SuppressLint("StaticFieldLeak")
    Context context;

    AppDatabase db;

    Retrofit retrofit;


    //main vars
    public MutableLiveData<Boolean> didShowLearningScreen = new MutableLiveData<>(false);

    public MutableLiveData<MULTIPLYER_ENUM> currentChosenMultipliyer = new MutableLiveData<>(MULTIPLYER_ENUM.MULTYPLIER_1x);

    //presentation vars
    public MutableLiveData<List<Coin>> coins = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<Float> balance = new MutableLiveData<>(0f);

    public MutableLiveData<Boolean> canGetRefferalBonus = new MutableLiveData<>(true);

    public MutableLiveData<Float> claimedBalance = new MutableLiveData<Float>(0f);

    public static MutableLiveData<Boolean> hasLoadedAd = new MutableLiveData<Boolean>(false);

    public static MutableLiveData<Boolean> doesNotHaveConnection = new MutableLiveData<Boolean>(false);

    public static MutableLiveData<Boolean> hadConnectionError = new MutableLiveData<Boolean>(false);

    public static MutableLiveData<CONNECTION_ERROR_ENUM> connectionErrorEnum = new MutableLiveData<CONNECTION_ERROR_ENUM>();

    public CountDownTimer coinTimer = null;

    //one-time vars
    private boolean userHasBeenJustCreated = false;

    @Inject
    MainViewModel(@ApplicationContext Context context, AppDatabase db, MainDataRepository mainDataRepository, Retrofit retrofit){
        this.mainDataRepository = mainDataRepository;
        this.context = context;
        this.db = db;
        this.retrofit = retrofit;
        Log.d(TagUtils.MAINVIEWMODELTAG,"Worked main");

        setUpObservers();
        setUpPresentationVars();
        loadAllData();
    }


    //main functions
    private void setUpObservers(){
        mainDataRepository.balance.observeForever(newValue -> {
            if(mainDataRepository.balance.getValue()-balance.getValue()==0||mainDataRepository.balance.getValue()-balance.getValue()>56){
                balance.postValue(newValue);
            }
        });

        mainDataRepository.claimedBalance.observeForever(newValue -> MethodUtils.safeSetValue(claimedBalance,newValue));

        mainDataRepository.currentChosenMultipliyer.observeForever(this::updateTimer);

        mainDataRepository.didShowLearning.observeForever(newValue -> didShowLearningScreen.setValue(newValue));

        mainDataRepository.enteredCode.observeForever(newValue -> canGetRefferalBonus.postValue(mainDataRepository.hasNotEnteredCode()));
    }

    public void loadAllData(){
        Log.d(TagUtils.MAINVIEWMODELTAG,"Fired offff");
        MainDataDao md = db.mainDataDao();
        AppDatabase.databaseWriteExecutor.execute(() -> {

            MainDataTable mdt = md.get();
            if(mdt==null||mdt.random_save_id==0){
                long exitTime = System.currentTimeMillis();
                if(MainDataRepository.random_for_save.getValue()==null&&MainDataRepository.tempRandom==null){

                    int rand = IntegerUtils.generateRandomInteger();
                    MethodUtils.safeSetValue(MainDataRepository.random_for_save,rand);
                    MainDataRepository.tempRandom = rand;
                    md.insert(new MainDataTable(rand,exitTime));
                    userHasBeenJustCreated = true;
                    Log.d("usercreated",""+userHasBeenJustCreated);
                    log("User with id"+StringUtils.generateDeviceIdentifier(rand)+" created");
                }
                else if(MainDataRepository.tempRandom!=null){
                    MethodUtils.safeSetValue(MainDataRepository.random_for_save,MainDataRepository.tempRandom);
                    md.insert(new MainDataTable(MainDataRepository.tempRandom,exitTime));
                    userHasBeenJustCreated = true;
                    log("User with id"+StringUtils.generateDeviceIdentifier(MainDataRepository.tempRandom)+" created");
                }
            }
            else{
                Log.d("APPDBTEST","referal"+mdt.referals);
                Log.d("APPDBTEST","random_id"+mdt.random_save_id);
                Log.d("APPDBTEST","claimed_balance"+mdt.claimed_balance);
                Log.d("APPDBTEST","exit_time"+mdt.exit_time);
                mainDataRepository.updateMainDataRepo(mdt, System.currentTimeMillis());
                Log.d("Testots of random",""+mdt.random_save_id);
                log("User with id"+StringUtils.generateDeviceIdentifier(mdt.random_save_id)+" got in OnCreate");
            }

            loadNetworkData();
        });
    }

    private void loadNetworkData(){
        if(isOnline()){
            Log.d(TagUtils.MAINVIEWMODELTAG+"identify1","identifier " + StringUtils.generateDeviceIdentifier());
            getConfigData();
            getUserData();
        }

        else{
            throwConnectionError(CONNECTION_ERROR_ENUM.LOAD_ALL_DATA_STARTUP);
        }
    }

    public void saveUserData(){

        MainService mainService = retrofit.create(MainService.class);

        Call<UserResponse> userResp = mainService.getUser(StringUtils.generateDeviceIdentifier());
        userResp.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("network", "PROBLEM");
                    UserResponse userResponse = response.body();
                    assert userResponse != null;
                    UserDTO user = userResponse.users.get(0);

                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        MainDataTable mdt = new MainDataTable(mainDataRepository,ConvertUtils.stringToDate(user.serverTime).getTime());
                        db.mainDataDao().update(mdt);
                        Log.d("networkTESTSdb2", String.valueOf(mdt.referals));
                        Log.d(TagUtils.MAINVIEWMODELTAG,user.serverTime);
                        Log.d(TagUtils.MAINVIEWMODELTAG, String.valueOf(mainDataRepository.referals.getValue()));
                        Log.d(TagUtils.MAINVIEWMODELTAG, String.valueOf(mainDataRepository.balance.getValue()));
                        Log.d(TagUtils.MAINVIEWMODELTAG, String.valueOf(mainDataRepository.enteredCode.getValue()));
                    });
                }
                else {
                    Log.d("network", "failure" + response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Log.d("network", "failure" + t.getMessage());
            }
        });
    }

    public void updateShowLearning(){
        Log.d("REPO!!","withdraw "+mainDataRepository.cooldownmillisUntilFinishedLiveData.getValue());
        MainDataDao md = db.mainDataDao();
        AppDatabase.databaseWriteExecutor.execute(() -> {

            MainDataTable mdt = md.get();
            MethodUtils.safeSetValue(mainDataRepository.didShowLearning,true);
            MethodUtils.safeSetValue(didShowLearningScreen,true);
            if(mdt==null||mdt.random_save_id==0){
                long exitTime = System.currentTimeMillis();
                if(MainDataRepository.random_for_save.getValue()==null&&MainDataRepository.tempRandom==null){

                    int rand = IntegerUtils.generateRandomInteger();
                    MethodUtils.safeSetValue(MainDataRepository.random_for_save,rand);
                    MainDataRepository.tempRandom = rand;
                    md.insert(new MainDataTable(rand,exitTime));
                    userHasBeenJustCreated = true;
                    log("User with id"+StringUtils.generateDeviceIdentifier(rand)+" created");
                }
                else if(MainDataRepository.tempRandom!=null){
                    MethodUtils.safeSetValue(MainDataRepository.random_for_save,MainDataRepository.tempRandom);
                    md.insert(new MainDataTable(MainDataRepository.tempRandom,exitTime));
                    userHasBeenJustCreated = true;
                    log("User with id"+StringUtils.generateDeviceIdentifier(MainDataRepository.tempRandom)+" created");
                }
            }
            else{
                Log.d("networkTESTSdb3", String.valueOf(mdt.referals));
                MainDataTable new_mdt = new MainDataTable(mdt.random_save_id,true,mdt.balance,mdt.claimed_balance,mdt.currentChosenMultipliyerValue,mdt.isActive,mdt.isActive,mdt.exit_time,mdt.estimated_end_time,mdt.cooldown_estimated_end_time, mdt.referals);
                md.update(new_mdt);
            }
        });
    }

    public static void throwConnectionError(CONNECTION_ERROR_ENUM errorEnum) {
        showSnackBar("Please, turn on Internet", false);
        MethodUtils.safeSetValue(MainViewModel.doesNotHaveConnection,true);
        MethodUtils.safeSetValue(MainViewModel.hadConnectionError,true);
        MethodUtils.safeSetValue(MainViewModel.connectionErrorEnum, errorEnum);
    }

    //helper functions
    private void getConfigData(){
        ConfigService configService = retrofit.create(ConfigService.class);
        Call<ConfigDTO> configResp = configService.getConfig(MainActivity.Companion.getLocale((Application) context));
        Log.d(TagUtils.MAINVIEWMODELTAG,"identifier " + StringUtils.generateDeviceIdentifier());
        configResp.enqueue(new Callback<ConfigDTO>() {
            @Override
            public void onResponse(@NonNull Call<ConfigDTO> call, @NonNull Response<ConfigDTO> response) {
                if (response.isSuccessful()) {
                    Log.d("network", "PROBLEM");
                    ConfigDTO configDTO = response.body();

                    assert configDTO != null;
                    MainDataRepository.unityID = configDTO.unityID;
                    MainDataRepository.unityBlock = configDTO.unityBlock;
                    MethodUtils.safeSetValue(mainDataRepository.urlGooglePlay,configDTO.partnerURL);
                    mainDataRepository.minValue = (float) configDTO.minValue;
                    mainDataRepository.maxValue = (float) configDTO.maxValue;
                    MethodUtils.safeSetValue(mainDataRepository.yourRefferalBonus,configDTO.referalBonusToUser);
                    MethodUtils.safeSetValue(mainDataRepository.otherRefferalBonus,configDTO.referalBonusForOthers);
                    mainDataRepository.convertValueToOneUsdt = configDTO.convertValueToOneUsdt;
                    mainDataRepository.widthdrawalDelay = configDTO.widthdrawalDelay;

                    MethodUtils.safeSetValue(mainDataRepository.learningText,configDTO.learningText);
                    MethodUtils.safeSetValue(mainDataRepository.refferalText1,configDTO.refferalText1);
                    MethodUtils.safeSetValue(mainDataRepository.refferealText2,configDTO.refferealText2);
                }
                else {
                    Log.d("network", "failure" + response.toString());
                    getDefaultConfigData();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ConfigDTO> call, @NonNull Throwable t) {
                Log.d("network", "failure" + t.getMessage());
                getDefaultConfigData();
            }
        });
    }
    private void getDefaultConfigData(){
        ConfigService configService = retrofit.create(ConfigService.class);

        Call<ConfigDTO> configResp = configService.getConfigDefault();
        Log.d(TagUtils.MAINVIEWMODELTAG,"identifier " + StringUtils.generateDeviceIdentifier());
        configResp.enqueue(new Callback<ConfigDTO>() {
            @Override
            public void onResponse(@NonNull Call<ConfigDTO> call, @NonNull Response<ConfigDTO> response) {
                if (response.isSuccessful()) {
                    Log.d("network", "PROBLEM");
                    ConfigDTO configDTO = response.body();

                    assert configDTO != null;
                    MainDataRepository.unityID = configDTO.unityID;
                    MainDataRepository.unityBlock = configDTO.unityBlock;
                    MethodUtils.safeSetValue(mainDataRepository.urlGooglePlay,configDTO.partnerURL);
                    mainDataRepository.minValue = (float) configDTO.minValue;
                    mainDataRepository.maxValue = (float) configDTO.maxValue;
                    MethodUtils.safeSetValue(mainDataRepository.yourRefferalBonus,configDTO.referalBonusToUser);
                    MethodUtils.safeSetValue(mainDataRepository.otherRefferalBonus,configDTO.referalBonusForOthers);
                    mainDataRepository.convertValueToOneUsdt = configDTO.convertValueToOneUsdt;
                    mainDataRepository.widthdrawalDelay = 1000*1000;//Todo change back

                    MethodUtils.safeSetValue(mainDataRepository.learningText,configDTO.learningText);
                    MethodUtils.safeSetValue(mainDataRepository.refferalText1,configDTO.refferalText1);
                    MethodUtils.safeSetValue(mainDataRepository.refferealText2,configDTO.refferealText2);
                }
                else {
                    Log.d("network", "failure" + response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ConfigDTO> call, @NonNull Throwable t) {
                Log.d("network", "failure" + t.getMessage());
            }
        });
    }
    private void getYourCodeBonus(Integer diff, Integer toSave){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> showToast("Congratulations, you received "+ diff + "m crypto points!"), 3200);
        log("User got his code entered by another user and received bonus");
        mainDataRepository.getRefferalOtherCodeBonus(diff);
        MethodUtils.safeSetValue(mainDataRepository.referals,toSave);
        saveUserData();
    }
    private void getUserData(){
        MainService mainService = retrofit.create(MainService.class);

        Log.d("Testots of random2",""+MainDataRepository.random_for_save.getValue());
        Call<UserResponse> userResp = mainService.getUser(StringUtils.generateDeviceIdentifier());
        Log.d(TagUtils.MAINVIEWMODELTAG+"identify2","identifier " + StringUtils.generateDeviceIdentifier());
        userResp.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("network", "PROBLEM");
                    UserResponse userResponse = response.body();
                    assert userResponse != null;
                    UserDTO user = userResponse.users.get(0);

                    Log.d(TagUtils.MAINVIEWMODELTAG,"ref code " + user.refCode);

                    MethodUtils.safeSetValue(mainDataRepository.referralCode, user.refCode);
                    MethodUtils.safeSetValue(mainDataRepository.enteredCode,user.enteredCode);

                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        MainDataTable mdt = db.mainDataDao().get();
                        Log.d("networkTESTS",""+user.boostTimer);
                        Log.d("networkTESTS",""+ConvertUtils.stringToDate(user.serverTime).getTime());
                        Log.d("networkTESTS",""+System.currentTimeMillis());
                        Log.d("networkTESTS",""+user.enteredCode);
                        Log.d("networkTESTS",""+user.id);
                        Log.d("networkTESTS",""+user.referals);
                        Log.d("networkTESTSdb4","referal"+mdt.referals);
                        Log.d("networkTESTSdb4","random_id"+mdt.random_save_id);
                        Log.d("networkTESTSdb4","claimed_balance"+mdt.claimed_balance);
                        Log.d("networkTESTSdb4","exit_time"+mdt.exit_time);
                        mainDataRepository.updateMainDataRepo(mdt,ConvertUtils.stringToDate(user.serverTime));
                        if(user.referals>mdt.referals){
                            getYourCodeBonus(user.referals-mdt.referals, user.referals);
                        }
                        setUpCooldown();
                    });
                }
                else {
                    Log.d(TagUtils.MAINVIEWMODELTAG,"failudre of network request");
                    Log.d("network", "failure" + response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Log.d("network", "failure" + t.getMessage());
            }
        });
    }
    private void setUpCooldown(){
        if(userHasBeenJustCreated){
            mainDataRepository.tryRefreshCooldown(mainDataRepository.widthdrawalDelay);
        }
    }


    //presentation functions
    private void setUpPresentationVars(){
        for(int i = 0; i<500; i++){
            Objects.requireNonNull(coins.getValue()).add(null);
        }
    }
    private void addCoin(){
        if(Boolean.TRUE.equals(mainDataRepository.isActive.getValue())&& Objects.requireNonNull(coins.getValue()).size()==500){

            if(coins.getValue()!=null){
                if(coins.getValue().get(0)==null)
                    Log.d("CoinTest0","null");
                else
                    Log.d("CoinTest0",coins.getValue().get(0).toString());
                if(coins.getValue().get(499)==null)
                    Log.d("CoinTest499","null");
                else
                    Log.d("CoinTest499",coins.getValue().get(499).toString());
            }
            if(coins.getValue().get(499)!=null){
                List<Coin> currentCoins = coins.getValue();
                for(int i = 0; i<100; i++){
                    if (currentCoins != null) {
                        currentCoins.set(i,null);
                    }
                }
                coins.setValue(currentCoins);
            }
            int j = 0;
            List<Coin> coinList = coins.getValue();

            while(coins.getValue().get(499) ==null){
                if(coins.getValue().get(j)==null){
                    Random random = new Random();
                    float x = random.nextFloat()*0.8f;
                    coinList.set(j,new Coin(j,random.nextFloat()+ 0.5f,x,0,x,0.8f));
                    coins.setValue(coinList);
                    break;
                }
                j++;
            }
        }
    }
    private void updateTimer(MULTIPLYER_ENUM count){
        if(count.getValue()!=0&&count.getValue()<=55){
            if(coinTimer!=null){
                coinTimer.cancel();
            }
            coinTimer = new CountDownTimer(fullTimerDuration, 1000/(count.ordinal()+1)) {
                public void onTick(long millisUntilFinished) {
                    if(millisUntilFinished<=500L){
                        if(coinTimer!=null){
                            coinTimer.cancel();
                        }
                    }

                    // For animation of coins
                    if (Boolean.FALSE.equals(MainActivity.Companion.isOnPaused().getValue())) {
                        addCoin();
                    }
                }
                public void onFinish() {
                    if(coinTimer!=null){
                        coinTimer.cancel();
                    }
                }
            }.start();
        }
    }
    public void removeCoin(Integer id) {
        List<Coin> currentCoins = coins.getValue();
        if (currentCoins != null) {
            Log.d("VIEWMODElend",id.toString());
            currentCoins.set(id,null);
            coins.setValue(currentCoins);
        }
    }
    public void emptyCoinList(){
        List<Coin> temp = coins.getValue();
        for(int i = 0; i<500; i++){
            assert temp != null;
            if(temp.get(i)==null){
                break;
            }
            else{
                temp.set(i,null);
            }
        }
        coins.setValue(temp);
    }

    //util functions
    public static void showSnackBar(String text, Boolean hasSucceded){
        if(Objects.equals(MainActivity.Companion.getSnackBarText().getValue(), text)){
            MethodUtils.safeSetValue(MainActivity.Companion.getSnackBarText(),text+" ");
        }
        else{
            MethodUtils.safeSetValue(MainActivity.Companion.getSnackBarText(),text);
        }
        MainActivity.Companion.setHasSucceded(hasSucceded);
    }
    public static void showToast(String text){
        MethodUtils.safeSetValue(MainActivity.Companion.getToastText(),text);
    }
    public boolean isOnline(){
        return MainActivity.Companion.isOnline((Context) context);
    }
    public static void log(String message){
        Log.d(TagUtils.MAINVIEWMODELTAG,message);
        LogService service = RetrofitClient.getClient().create(LogService.class);
        Call<LogResponse> log_call = service.log(StringUtils.generateDeviceIdentifier(),message,MainActivity.Companion.getSource());
        log_call.enqueue(new Callback<LogResponse>() {
            @Override
            public void onResponse(@NonNull Call<LogResponse> call, @NonNull Response<LogResponse> response) {
                if (response.isSuccessful()) {
                    LogResponse adsResponse = response.body();
                    assert adsResponse != null;
                    if(adsResponse.success==1){
                        Log.d("network","got it right"+adsResponse.message);
                    }
                    else{
                        Log.d("network","got it wrong"+adsResponse.message);
                    }
                } else {
                    Log.d("network", "failure" + response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<LogResponse> call, @NonNull Throwable t) {
                Log.d("network", "failure" + t.getMessage());
            }
        });
    }
}
