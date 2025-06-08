package com.johov.bitcoin.viewmodel;

import static com.johov.bitcoin.data.repository.MainDataRepository.fullTimerDuration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Retrofit;
import com.johov.bitcoin.MainActivity;
import com.johov.bitcoin.data.db.AppDatabase;
import com.johov.bitcoin.data.db.maindata.MainDataDao;
import com.johov.bitcoin.data.db.maindata.MainDataTable;
import com.johov.bitcoin.data.model.enums.CONNECTION_ERROR_ENUM;
import com.johov.bitcoin.data.model.enums.MULTIPLYER_ENUM;
import com.johov.bitcoin.data.repository.MainDataRepository;
import com.johov.bitcoin.ui.view.composables.Coin;
import com.johov.bitcoin.utils.IntegerUtils;
import com.johov.bitcoin.utils.MethodUtils;
import com.johov.bitcoin.utils.StringUtils;
import com.johov.bitcoin.utils.TagUtils;

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
    public static boolean userHasBeenJustCreated = false;

    @Inject
    MainViewModel(@ApplicationContext Context context, AppDatabase db, MainDataRepository mainDataRepository){
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
                mainDataRepository.updateMainDataRepo(mdt);
                Log.d("Testots of random",""+mdt.random_save_id);
                log("User with id"+StringUtils.generateDeviceIdentifier(mdt.random_save_id)+" got in OnCreate");
            }

            loadPseudoNetworkData();
        });
    }

    private void loadPseudoNetworkData(){
        if(isOnline()){
            Log.d(TagUtils.MAINVIEWMODELTAG+"identify1","identifier " + StringUtils.generateDeviceIdentifier());
            getConfigData();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() ->{
                Log.d("NETWORKTAG3",""+MainDataRepository.unityID);
                Log.d("NETWORKTAG3",""+MainDataRepository.unityBlock);
                getUserData();
            }, 2000);
        }

        else{
            throwConnectionError(CONNECTION_ERROR_ENUM.LOAD_ALL_DATA_STARTUP);
        }
    }

    public void saveUserData(){

        AppDatabase.databaseWriteExecutor.execute(() -> {
            MainDataTable mdt = new MainDataTable(mainDataRepository,System.currentTimeMillis());
            db.mainDataDao().update(mdt);
            Log.d("networkTESTSdb2", String.valueOf(mdt.referals));
            Log.d(TagUtils.MAINVIEWMODELTAG,""+System.currentTimeMillis());
            Log.d(TagUtils.MAINVIEWMODELTAG, String.valueOf(mainDataRepository.referals.getValue()));
            Log.d(TagUtils.MAINVIEWMODELTAG, String.valueOf(mainDataRepository.balance.getValue()));
            Log.d(TagUtils.MAINVIEWMODELTAG, String.valueOf(mainDataRepository.enteredCode.getValue()));
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
                MainDataTable new_mdt = new MainDataTable(mdt.random_save_id,true,mdt.balance,mdt.claimed_balance,mdt.currentChosenMultipliyerValue,mdt.isActive,mdt.isActive,mdt.exit_time,mdt.estimated_end_time,mdt.cooldown_estimated_end_time, mdt.referals, mdt.did_show_rate_window);
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

        MainDataRepository.unityID = "5843451";
        MainDataRepository.unityBlock = "Rewarded_Android";
        MethodUtils.safeSetValue(mainDataRepository.urlGooglePlay,"https://play.google.com/store/apps/details?id=com.binance.dev");
        mainDataRepository.minValue = (float) 0.01;
        mainDataRepository.maxValue = (float) 49.74;
        mainDataRepository.miningBaseValue = (float) 0.00000001;
        MethodUtils.safeSetValue(mainDataRepository.yourRefferalBonus,0.0001f);
        MethodUtils.safeSetValue(mainDataRepository.otherRefferalBonus,0.0001f);
        mainDataRepository.convertValueToOneUsdt = 0.0001f;
        mainDataRepository.widthdrawalDelay = 24 * 60 * 60 * 1000L; //todo remove for debug
        mainDataRepository.withdrawalRateTriggerSum = 1.6f;//new

        MethodUtils.safeSetValue(mainDataRepository.learningText,"Welcome your fellows and get \n" +
                "                                                                                                     0.0001 Bitcoin");
        MethodUtils.safeSetValue(mainDataRepository.refferalText1,"Welcome your fellows and receive 0.0001 Bitcoin, each invited friend will receive 0.0001 Bitcoin");
        MethodUtils.safeSetValue(mainDataRepository.refferealText2,"Enter my referral code #CODE# and receive 0.0001 Bitcoin in the Bitcoin Earner application - #LINK#");
        MethodUtils.safeSetValue(MainDataRepository.didCorrectlyLaunch,true);

    }
    private void getUserData(){

        MethodUtils.safeSetValue(mainDataRepository.referralCode, StringUtils.generateDeviceIdentifier());
        MethodUtils.safeSetValue(mainDataRepository.enteredCode,"");

        AppDatabase.databaseWriteExecutor.execute(() -> {
            MainDataTable mdt = db.mainDataDao().get();
            mainDataRepository.updateMainDataRepo(mdt, System.currentTimeMillis());
            setUpCooldown();
        });
    }

    private void setUpCooldown(){
        Log.d("COOLDOWN",""+userHasBeenJustCreated);
        Log.d("COOLDOWN",""+mainDataRepository.widthdrawalDelay);
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
        Log.d("Log Center","The current message is: "+message);
    }
}
