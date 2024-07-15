package silmex.apps.airdropcryptopoints.viewmodel;

import static silmex.apps.airdropcryptopoints.data.repository.MainDataRepository.fullTimerDuration;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import dagger.Provides;
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
import silmex.apps.airdropcryptopoints.data.model.MULTIPLYER_ENUM;
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

    public MutableLiveData<Float> balance = new MutableLiveData<Float>(0f);

    public static MutableLiveData<Boolean> hasLoadedAd = new MutableLiveData<Boolean>(false);

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
        mainDataRepository.balance.observeForever(new Observer<Float>() {
            @Override
            public void onChanged(Float newValue) {
                if(mainDataRepository.balance.getValue()-balance.getValue()==0||mainDataRepository.balance.getValue()-balance.getValue()>56){
                    balance.postValue(newValue);
                }
                else{
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        MainDataTable mdt = new MainDataTable(mainDataRepository,System.currentTimeMillis());
                        db.mainDataDao().update(mdt);
                        Log.d("Balance","updated");
                    });
                }
            }
        });
        mainDataRepository.currentChosenMultipliyer.observeForever(new Observer<MULTIPLYER_ENUM>() {
            @Override
            public void onChanged(MULTIPLYER_ENUM newValue) {
                updateTimer(newValue);
            }
        });
        mainDataRepository.didShowLearning.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean newValue) {
                didShowLearningScreen.setValue(newValue);
            }
        });
    }
    //buisness logic

    private void loadAllData(){
        MainDataDao md = db.mainDataDao();
        AppDatabase.databaseWriteExecutor.execute(() -> {

            MainDataTable mdt = md.get();
            if(mdt==null){
                long exitTime = System.currentTimeMillis();
                MainDataRepository.random_for_save = IntegerUtils.generateRandomInteger();
                md.insert(new MainDataTable(MainDataRepository.random_for_save,exitTime));
                userHasBeenJustCreated = true;
                log("User with id"+StringUtils.generateDeviceIdentifier()+" created");
            }
            else{
                mainDataRepository.updateMainDataRepo(mdt, System.currentTimeMillis());
                log("User with id"+StringUtils.generateDeviceIdentifier()+" got in OnCreate");
            }


            loadNetworkData();
        });
    }

    private void loadNetworkData(){
        if(isOnline()){
            getConfigData();
            getUserData();
        }

        else{
            showToast("Turn on Internet",false);
        }
    }


    public void saveUserData(){

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
                        MainDataTable mdt = new MainDataTable(mainDataRepository,ConvertUtils.stringToDate(user.serverTime).getTime());
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

    public void updateShowLearning(){
        MainDataDao md = db.mainDataDao();
        AppDatabase.databaseWriteExecutor.execute(() -> {

            MainDataTable mdt = md.get();
            MethodUtils.safeSetValue(mainDataRepository.didShowLearning,true);
            MethodUtils.safeSetValue(didShowLearningScreen,true);
            if(mdt==null){
                long exitTime = System.currentTimeMillis();
                MainDataRepository.random_for_save = IntegerUtils.generateRandomInteger();
                md.insert(new MainDataTable(MainDataRepository.random_for_save,exitTime));
                userHasBeenJustCreated = true;
                log("User with id"+StringUtils.generateDeviceIdentifier()+" created");
            }
            else{
                MainDataTable new_mdt = new MainDataTable(mdt.random_save_id,true,mdt.balance,mdt.currentChosenMultipliyerValue,mdt.isActive,mdt.isActive,mdt.exit_time,mdt.estimated_end_time,mdt.cooldown_estimated_end_time, mdt.referals);
                md.update(new_mdt);
            }
        });
    }

    //helper functions
    private void getUserData(){
        MainService mainService = retrofit.create(MainService.class);

        Call<UserResponse> userResp = mainService.getUser(MainDataRepository.geteDeviceIdentifier());
        Log.d(TagUtils.MAINVIEWMODELTAG,"identifier " + MainDataRepository.geteDeviceIdentifier());
        userResp.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("network", "PROBLEM");
                    UserResponse userResponse = response.body();
                    UserDTO user = userResponse.users.get(0);

                    Log.d(TagUtils.MAINVIEWMODELTAG,"ref code " + user.refCode);

                    MethodUtils.safeSetValue(mainDataRepository.referralCode, user.refCode);
                    mainDataRepository.enteredCode = user.enteredCode;

                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        MainDataTable mdt = db.mainDataDao().get();
                        Log.d("networkTESTS",""+user.boostTimer);
                        Log.d("networkTESTS",""+ConvertUtils.stringToDate(user.serverTime).getTime());
                        Log.d("networkTESTS",""+System.currentTimeMillis());
                        Log.d("networkTESTS",""+user.enteredCode);
                        Log.d("networkTESTS",""+user.id);
                        Log.d("networkTESTS",""+user.referals);
                        mainDataRepository.updateMainDataRepo(mdt,ConvertUtils.stringToDate(user.serverTime));// TODO before this method ther eis a save somewhere, needs to be removed
                        if(user.referals>mdt.referals){
                            getYourCodeBonus(user.referals-mdt.referals, user.referals);
                        }
                        setUpCooldown(mdt.cooldown_estimated_end_time);
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
    private void getConfigData(){
        ConfigService configService = retrofit.create(ConfigService.class);

        Call<ConfigDTO> configResp = configService.getConfig(MainActivity.Companion.getLocale((Application) context));
        Log.d(TagUtils.MAINVIEWMODELTAG,"identifier " + MainDataRepository.geteDeviceIdentifier());
        configResp.enqueue(new Callback<ConfigDTO>() {
            @Override
            public void onResponse(Call<ConfigDTO> call, Response<ConfigDTO> response) {
                if (response.isSuccessful()) {
                    Log.d("network", "PROBLEM");
                    ConfigDTO configDTO = response.body();

                    MainDataRepository.unityID = configDTO.unityID;
                    MainDataRepository.unityBlock = configDTO.unityBlock;
                    MethodUtils.safeSetValue(mainDataRepository.urlGooglePlay,configDTO.unityBlock);
                    mainDataRepository.minValue = (float) configDTO.minValue;
                    mainDataRepository.maxValue = (float) configDTO.maxValue;
                    mainDataRepository.yourRefferalBonus = configDTO.referalBonusToUser;
                    mainDataRepository.otherRefferalBonus = configDTO.referalBonusForOthers;
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
            public void onFailure(Call<ConfigDTO> call, Throwable t) {
                Log.d("network", "failure" + t.getMessage());
                getDefaultConfigData();
            }
        });
    }
    private void getDefaultConfigData(){
        ConfigService configService = retrofit.create(ConfigService.class);

        Call<ConfigDTO> configResp = configService.getConfigDefault();
        Log.d(TagUtils.MAINVIEWMODELTAG,"identifier " + MainDataRepository.geteDeviceIdentifier());
        configResp.enqueue(new Callback<ConfigDTO>() {
            @Override
            public void onResponse(Call<ConfigDTO> call, Response<ConfigDTO> response) {
                if (response.isSuccessful()) {
                    Log.d("network", "PROBLEM");
                    ConfigDTO configDTO = response.body();

                    MainDataRepository.unityID = configDTO.unityID;
                    MainDataRepository.unityBlock = configDTO.unityBlock;
                    MethodUtils.safeSetValue(mainDataRepository.urlGooglePlay,configDTO.unityBlock);
                    mainDataRepository.minValue = (float) configDTO.minValue;
                    mainDataRepository.maxValue = (float) configDTO.maxValue;
                    mainDataRepository.yourRefferalBonus = configDTO.referalBonusToUser;
                    mainDataRepository.otherRefferalBonus = configDTO.referalBonusForOthers;
                    mainDataRepository.convertValueToOneUsdt = configDTO.convertValueToOneUsdt;
                    mainDataRepository.widthdrawalDelay = 100*1000;//Todo change back

                    MethodUtils.safeSetValue(mainDataRepository.learningText,configDTO.learningText);
                    MethodUtils.safeSetValue(mainDataRepository.refferalText1,configDTO.refferalText1);
                    MethodUtils.safeSetValue(mainDataRepository.refferealText2,configDTO.refferealText2);
                }
                else {
                    Log.d("network", "failure" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<ConfigDTO> call, Throwable t) {
                Log.d("network", "failure" + t.getMessage());
            }
        });
    }

    private void getYourCodeBonus(Integer diff, Integer toSave){
        mainDataRepository.getRefferalOtherCodeBonus(diff);
        mainDataRepository.referals = toSave;
        saveUserData();
    }

    private void setUpCooldown(long delay){
        if(userHasBeenJustCreated){
            mainDataRepository.tryRefreshCooldown(mainDataRepository.widthdrawalDelay);
        }
    }


    //presentation functions
    private void setUpPresentationVars(){
        for(int i = 0; i<500; i++){
            coins.getValue().add(null);
        }
    }
    private void addCoin(){
        if(Boolean.TRUE.equals(mainDataRepository.isActive.getValue())&&coins.getValue().size()==500){

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
            if(temp.get(i)==null){
                break;
            }
            else{
                temp.set(i,null);
            }
        }
        coins.setValue(temp);
    }
    private void showToast(String text,Boolean hasSucceded){
        MainActivity.Companion.setHasSucceded(hasSucceded);
        MainActivity.Companion.getToastText().setValue(text);
    }

    //util functions
    public boolean isOnline(){
        return MainActivity.Companion.isOnline((Context) context);
    }

    public static void log(String message){

        LogService service = RetrofitClient.getClient().create(LogService.class);
        Call<LogResponse> log_call = service.log(MainDataRepository.geteDeviceIdentifier(),message,MainActivity.Companion.getSource());
        log_call.enqueue(new Callback<LogResponse>() {
            @Override
            public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                if (response.isSuccessful()) {
                    LogResponse adsResponse = response.body();
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
            public void onFailure(Call<LogResponse> call, Throwable t) {
                Log.d("network", "failure" + t.getMessage());
            }
        });
    }
}
