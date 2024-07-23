package silmex.apps.airdropcryptopoints.data.repository;

import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import silmex.apps.airdropcryptopoints.data.db.maindata.MainDataTable;
import silmex.apps.airdropcryptopoints.data.model.enums.MULTIPLYER_ENUM;
import silmex.apps.airdropcryptopoints.data.model.Transaction;
import silmex.apps.airdropcryptopoints.utils.IntegerUtils;
import silmex.apps.airdropcryptopoints.utils.MethodUtils;
import silmex.apps.airdropcryptopoints.utils.TagUtils;

public class MainDataRepository {

    //constructor
    public MainDataRepository(){

    }


    //vars for configing app
    public static MutableLiveData<Integer> random_for_save = new MutableLiveData<>(null);
    public static Integer tempRandom = null;
    public static String unityID = "5457496";
    public static String unityBlock = "Rewarded_Android";

    //main data vars
    public MutableLiveData<Boolean> didShowLearning = new MutableLiveData<Boolean>(false);
    public MutableLiveData<Float> balance = new MutableLiveData<Float>(0F);
    public MutableLiveData<Float> claimedBalance = new MutableLiveData<Float>(0F);
    public MutableLiveData<MULTIPLYER_ENUM> currentChosenMultipliyer = new MutableLiveData<MULTIPLYER_ENUM>(MULTIPLYER_ENUM.MULTYPLIER_1x);
    public MutableLiveData<Boolean> isActive = new MutableLiveData<>(false);
    public Date serverTime = null;


    //referral data vars
    public MutableLiveData<Integer> referralCode = new MutableLiveData<Integer>(Math.abs(IntegerUtils.generateRandomInteger()));
    public MutableLiveData<String> enteredCode = new MutableLiveData<>("");
    public MutableLiveData<Integer> yourRefferalBonus = new MutableLiveData<>(1000000);
    public MutableLiveData<Integer> otherRefferalBonus =  new MutableLiveData<>(1000000);
    public MutableLiveData<Integer> referals = new MutableLiveData<>(0);
    public boolean hasNotEnteredCode(){
        return enteredCode.getValue().isEmpty();
    }


    //withdrawal data vars
    public MutableLiveData<List<Transaction>> transactionList = new MutableLiveData<List<Transaction>>(new ArrayList<Transaction>());
    public MutableLiveData<String> urlGooglePlay = new MutableLiveData<>("https://t.me/finsignal");
    public float minValue =0.01f;
    public float maxValue =49.74f;
    public int convertValueToOneUsdt =198000;
    public long widthdrawalDelay =1000*1000;//Todo change to 86400000
    public MutableLiveData<Boolean> canWithdraw =new MutableLiveData<>(false);


    //presentation vars
    public MutableLiveData<String> learningText = new MutableLiveData<String>("Invite friends and get \n1 000 000 Crypto Points");
    public MutableLiveData<String> refferalText1 = new MutableLiveData<String>("Invite friends and receive 1 000 000 crypto points, each invited friend will receive 1 000 000 crypto points");
    public MutableLiveData<String> refferealText2 = new MutableLiveData<String>("Enter my referral code #CODE# and receive 1 000 000 crypto points in the AirDrop Crypto Points application - #LINK#");


    //timer vars
    public CountDownTimer mainTimer;
    public CountDownTimer withdrawalCooldownTimer;
    public static final long fullTimerDuration = 14400000;//TODO change to 14400000
    public MutableLiveData<Long> millisUntilFinishedLiveData = new MutableLiveData<>(0L);
    public MutableLiveData<Long> cooldownmillisUntilFinishedLiveData = new MutableLiveData<>(0L);
    public long tempLeftTime = 0;



    //main vars setters
    //timer setters
    public void setTimer(){
        if(Boolean.FALSE.equals(isActive.getValue())&&mainTimer==null){

            MethodUtils.safeSetValue(isActive,true);
            mainTimer = new CountDownTimer(fullTimerDuration, 1000) {
                public void onTick(long millisUntilFinished) {
                    if(millisUntilFinished<=500L){
                        turnOffTimer();
                    }
                    MethodUtils.safeSetValue(balance,Objects.requireNonNull(balance.getValue())+ Objects.requireNonNull(currentChosenMultipliyer.getValue()).getValue());
                    MethodUtils.safeSetValue(millisUntilFinishedLiveData,millisUntilFinished);
                }
                public void onFinish() {
                    turnOffTimer();
                }
            }.start();
        }
    }
    public void updateTimer(long duration){
            MethodUtils.safeSetValue(isActive,true);

            mainTimer = new CountDownTimer(duration, 1000) {
                public void onTick(long millisUntilFinished) {

                    if(millisUntilFinished<=500L){
                        turnOffTimer();
                    }
                    MethodUtils.safeSetValue(balance,Objects.requireNonNull(balance.getValue())+ Objects.requireNonNull(currentChosenMultipliyer.getValue()).getValue());
                    MethodUtils.safeSetValue(millisUntilFinishedLiveData,millisUntilFinished);
                }
                public void onFinish() {

                    turnOffTimer();
                }
            }.start();
    }
    public void turnOffTimer(){
        MethodUtils.safeSetValue(currentChosenMultipliyer,MULTIPLYER_ENUM.MULTYPLIER_1x);

        MethodUtils.safeSetValue(isActive,false);

        if(mainTimer!=null){

            mainTimer.cancel();
            mainTimer = null;
        }
    }

    public void refreshCooldown(long estimatedEndTime){
        Log.d("Repo","refreshed "+estimatedEndTime);
            MethodUtils.safeSetValue(canWithdraw,false);
            withdrawalCooldownTimer = new CountDownTimer(estimatedEndTime, 1000) {
                public void onTick(long millisUntilFinished) {
                    if(millisUntilFinished<=500L){
                        turnOffCooldownTimer();
                    }
                    MethodUtils.safeSetValue(cooldownmillisUntilFinishedLiveData,millisUntilFinished);
                }
                public void onFinish() {
                    turnOffCooldownTimer();
                }
            }.start();
    }

    public void turnOffCooldownTimer(){

        Log.d("Repo","turned off cooldown");
        MethodUtils.safeSetValue(canWithdraw,true);
        if(withdrawalCooldownTimer!=null){
            withdrawalCooldownTimer.cancel();
            withdrawalCooldownTimer = null;
        }
    }

    public void tryRefreshCooldown(long estimatedTime){
        Log.d("Repo","tried to refreshed ");
        new Handler(Looper.getMainLooper()).post(() -> refreshCooldown(estimatedTime));
    }

    //home setters
    public void claimBalance(){
        MethodUtils.safeSetValue(currentChosenMultipliyer,MULTIPLYER_ENUM.MULTYPLIER_1x);
        MethodUtils.safeSetValue(claimedBalance,claimedBalance.getValue()+balance.getValue());
        MethodUtils.safeSetValue(balance,0f);
    }

    //refferral setters
    public void getRefferalYourCodeBonus(Integer diff){
        if(yourRefferalBonus.getValue()!=null){
            MethodUtils.safeSetValue(claimedBalance,claimedBalance.getValue()+(yourRefferalBonus.getValue()*diff));
        }
        else{
            MethodUtils.safeSetValue(claimedBalance,claimedBalance.getValue()+(1000000*diff));
        }
        MethodUtils.safeSetValue(enteredCode,"plug");
    }
    public void getRefferalOtherCodeBonus(Integer diff){
        if(otherRefferalBonus.getValue()!=null){
            MethodUtils.safeSetValue(claimedBalance,claimedBalance.getValue()+(otherRefferalBonus.getValue()*diff));
        }
        else{
            MethodUtils.safeSetValue(claimedBalance,claimedBalance.getValue()+(1000000*diff));
        }
    }

    //withdrawal setters
    public void resetBalance(){
        MethodUtils.safeSetValue(claimedBalance,0f);
    }
    public Float getBalanceForWithdrawal(){
        int crypto_points = Objects.requireNonNull(claimedBalance.getValue()).intValue();
        float bucks = (float) crypto_points /convertValueToOneUsdt;
        if(bucks<minValue){
            return minValue;
        }
        return Math.min(bucks, maxValue);
    }

    //update when loading app
    public void updateMainDataRepo(MainDataTable mdt, Long currentTime){
        MethodUtils.safeSetValue(random_for_save,mdt.random_save_id);
        MethodUtils.safeSetValue(referals,mdt.referals);
        tempRandom = mdt.random_save_id;
        Log.d(TagUtils.MAINVIEWMODELTAG,"Worked db");

        MethodUtils.safeSetValue(didShowLearning,mdt.didShowLearning);
        MethodUtils.safeSetValue(currentChosenMultipliyer,MULTIPLYER_ENUM.getEnumValue(mdt.currentChosenMultipliyerValue));
        MethodUtils.safeSetValue(isActive,mdt.isActive);
        MethodUtils.safeSetValue(balance,mdt.balance);
        MethodUtils.safeSetValue(claimedBalance,mdt.claimed_balance);
    }
    public void updateMainDataRepo(MainDataTable mdt, Date serverTime){
        MethodUtils.safeSetValue(referals,mdt.referals);
        MethodUtils.safeSetValue(claimedBalance,mdt.claimed_balance);
        this.serverTime = serverTime;
        if(mdt.isActive){
            tempLeftTime  = (serverTime.getTime() - mdt.exit_time)/1000L;
            long timerDiffernce = (tempLeftTime - mdt.estimated_end_time/1000L);

            Log.d("Values",""+mdt.exit_time);
            Log.d("Values",""+serverTime);
            Log.d("Values",""+serverTime.getTime());
            Log.d("Values",""+tempLeftTime);
            Log.d("Values",""+mdt.estimated_end_time/1000L);
            Log.d("Values",""+timerDiffernce);

            if(timerDiffernce<=0){

                MethodUtils.safeSetValue(balance,mdt.balance + mdt.currentChosenMultipliyerValue*Math.abs(tempLeftTime));
                MethodUtils.safeSetValue(millisUntilFinishedLiveData,Math.abs(timerDiffernce*1000L));

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Values",""+timerDiffernce*1000L);
                        updateTimer(Math.abs(timerDiffernce*1000L));
                    }
                });
            }
            else{
                MethodUtils.safeSetValue(balance,mdt.balance + mdt.currentChosenMultipliyerValue*(mdt.estimated_end_time/1000L));
                MethodUtils.safeSetValue(currentChosenMultipliyer,MULTIPLYER_ENUM.getEnumValue(1));
                MethodUtils.safeSetValue(isActive,false);
            }
        }
        else{
            MethodUtils.safeSetValue(balance,mdt.balance);
        }
        if(Boolean.FALSE.equals(canWithdraw.getValue())||mdt.cooldown_estimated_end_time==0){
            long temp  = (serverTime.getTime() - mdt.exit_time);
            Log.d("Repo111","fired off" +temp);
            Log.d("Repo111","fired off" +mdt.exit_time);
            Log.d("Repo111","fired off" +serverTime.getTime());
            if(temp-mdt.cooldown_estimated_end_time<=0){
                Log.d("Repo111","fired off" +(mdt.cooldown_estimated_end_time-temp));
                tryRefreshCooldown(mdt.cooldown_estimated_end_time-temp);
            }
            else{
                MethodUtils.safeSetValue(canWithdraw,true);
                MethodUtils.safeSetValue(cooldownmillisUntilFinishedLiveData,0L);
            }
        }
        else{
            MethodUtils.safeSetValue(canWithdraw,mdt.can_withdraw);
            MethodUtils.safeSetValue(cooldownmillisUntilFinishedLiveData,0L);
        }
    }
}
