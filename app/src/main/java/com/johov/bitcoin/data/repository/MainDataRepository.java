package com.johov.bitcoin.data.repository;

import android.icu.math.BigDecimal;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.johov.bitcoin.data.db.maindata.MainDataTable;
import com.johov.bitcoin.data.model.enums.MULTIPLYER_ENUM;
import com.johov.bitcoin.data.model.Transaction;
import com.johov.bitcoin.utils.IntegerUtils;
import com.johov.bitcoin.utils.MethodUtils;
import com.johov.bitcoin.utils.TagUtils;
import com.johov.bitcoin.viewmodel.MainViewModel;

public class MainDataRepository {

    //constructor
    public MainDataRepository(){

    }


    //vars for configing app
    public static MutableLiveData<Integer> random_for_save = new MutableLiveData<>(null);
    public static Integer tempRandom = null;
    public static String unityID = "5457496";
    public static String unityBlock = "Rewarded_Android";
    public static MutableLiveData<Boolean> didShowRateWindow = new MutableLiveData<>(false);//new
    public static MutableLiveData<Boolean> didCorrectlyLaunch = new MutableLiveData<>(false);//new


    //main data vars
    public MutableLiveData<Boolean> didShowLearning = new MutableLiveData<Boolean>(false);
    public MutableLiveData<Float> balance = new MutableLiveData<Float>(0F);
    public MutableLiveData<Float> claimedBalance = new MutableLiveData<Float>(0F);
    public MutableLiveData<MULTIPLYER_ENUM> currentChosenMultipliyer = new MutableLiveData<MULTIPLYER_ENUM>(MULTIPLYER_ENUM.MULTYPLIER_1x);
    public MutableLiveData<Boolean> isActive = new MutableLiveData<>(false);
    public Long serverTime = null;


    //referral data vars
    public MutableLiveData<String> referralCode = new MutableLiveData<String>();
    public MutableLiveData<String> enteredCode = new MutableLiveData<>("");
    public MutableLiveData<Float> yourRefferalBonus = new MutableLiveData<>(1f);
    public static MutableLiveData<Float> otherRefferalBonus =  new MutableLiveData<>(1f);
    public MutableLiveData<Integer> referals = new MutableLiveData<>(0);
    public boolean hasNotEnteredCode(){
        return enteredCode.getValue().isEmpty();
    }


    //withdrawal data vars
    public MutableLiveData<List<Transaction>> transactionList = new MutableLiveData<List<Transaction>>(new ArrayList<Transaction>());
    public MutableLiveData<String> urlGooglePlay = new MutableLiveData<>("https://t.me/finsignal");
    public float minValue =0.01f;
    public float maxValue =49.74f;
    public float miningBaseValue = 0.000001f;
    public float convertValueToOneUsdt =0.16f;
    public long widthdrawalDelay =86400000L;
    public float withdrawalRateTriggerSum = 1.6f;//new
    public MutableLiveData<Boolean> canWithdraw =new MutableLiveData<>(false);


    //presentation vars
    public MutableLiveData<String> learningText = new MutableLiveData<String>("Invite friends and get \n 1 BTC");
    public MutableLiveData<String> refferalText1 = new MutableLiveData<String>("Invite friends and receive 1 BTC, each invited friend will receive \n1 BTC");
    public MutableLiveData<String> refferealText2 = new MutableLiveData<String>("Enter my referral code #CODE# and receive 1 BTC in the Bitcoin Earner application - #LINK#");


    //timer vars
    public CountDownTimer mainTimer;
    public CountDownTimer withdrawalCooldownTimer;
    public static final long fullTimerDuration = 14400000L;//todo remove back
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
                    MethodUtils.safeSetValue(balance,Objects.requireNonNull(balance.getValue())+ Objects.requireNonNull(currentChosenMultipliyer.getValue()).getValue()*miningBaseValue);
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
                    MethodUtils.safeSetValue(balance,Objects.requireNonNull(balance.getValue())+ Objects.requireNonNull(currentChosenMultipliyer.getValue()).getValue()*miningBaseValue);
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
        Log.d("Repo","refreshed in the cooldown"+estimatedEndTime);
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
        MethodUtils.safeSetValue(balance,0.0f);
    }

    //refferral setters
    public void getRefferalYourCodeBonus(Integer diff){
        BigDecimal a = new BigDecimal(claimedBalance.getValue());
        a = a.add(new BigDecimal(0.00000000001f));
        if(yourRefferalBonus.getValue()!=null){
            BigDecimal b = new BigDecimal(yourRefferalBonus.getValue()*diff);

            BigDecimal result = a.add(b);
            MethodUtils.safeSetValue(claimedBalance,result.floatValue());
        }
        else{
            BigDecimal b = new BigDecimal(0.0001f*diff);

            BigDecimal result = a.add(b);
            MethodUtils.safeSetValue(claimedBalance,result.floatValue());
        }
        MethodUtils.safeSetValue(enteredCode,"plug");
    }
    public void getRefferalOtherCodeBonus(Integer diff){
        BigDecimal a = new BigDecimal(claimedBalance.getValue());
        a = a.add(new BigDecimal(0.00000000001f));
        if(otherRefferalBonus.getValue()!=null){
            BigDecimal b = new BigDecimal(otherRefferalBonus.getValue()*diff);

            BigDecimal result = a.add(b);
            MethodUtils.safeSetValue(claimedBalance,result.floatValue());
        }
        else{
            BigDecimal b = new BigDecimal(0.0001f*diff);

            BigDecimal result = a.add(b);
            MethodUtils.safeSetValue(claimedBalance,result.floatValue());
        }
    }

    //withdrawal setters
    public void resetBalance(){
        MethodUtils.safeSetValue(claimedBalance,0f);
    }

    public Float getBalanceForWithdrawal(){
        Float test_points = claimedBalance.getValue();
        if(test_points==null) return null;
        float bucks = (float) test_points /convertValueToOneUsdt;

        Log.d("WTFFF",""+test_points);
        Log.d("WTFFF",""+bucks);
        Log.d("WTFFF",""+claimedBalance.getValue());
        if(bucks==0){
            return null;
        }
        if(bucks<minValue){
            return minValue;
        }
        return Math.min(bucks, maxValue);
    }

    //update when loading app
    public void updateMainDataRepo(MainDataTable mdt){
        MethodUtils.safeSetValue(random_for_save,mdt.random_save_id);
        MethodUtils.safeSetValue(referals,mdt.referals);
        tempRandom = mdt.random_save_id;
        MethodUtils.safeSetValue(didShowRateWindow,mdt.did_show_rate_window);//new
        Log.d(TagUtils.MAINVIEWMODELTAG,"Worked db");

        MethodUtils.safeSetValue(didShowLearning,mdt.didShowLearning);
        MethodUtils.safeSetValue(currentChosenMultipliyer,MULTIPLYER_ENUM.getEnumValue(mdt.currentChosenMultipliyerValue));
        MethodUtils.safeSetValue(isActive,mdt.isActive);
        MethodUtils.safeSetValue(balance,mdt.balance);
        MethodUtils.safeSetValue(claimedBalance,mdt.claimed_balance);
    }
    public void updateMainDataRepo(MainDataTable mdt, Long serverTime){
        MethodUtils.safeSetValue(referals,mdt.referals);
        MethodUtils.safeSetValue(claimedBalance,mdt.claimed_balance);
        MethodUtils.safeSetValue(didShowRateWindow,mdt.did_show_rate_window);
        this.serverTime = serverTime;
        if(mdt.isActive){
            tempLeftTime  = (serverTime - mdt.exit_time)/1000L;
            long timerDiffernce = (tempLeftTime - mdt.estimated_end_time/1000L);

            Log.d("Values",""+mdt.exit_time);
            Log.d("Values",""+serverTime);
            Log.d("Values",""+serverTime);
            Log.d("Values",""+tempLeftTime);
            Log.d("Values",""+mdt.estimated_end_time/1000L);
            Log.d("Values",""+timerDiffernce);

            if(timerDiffernce<=0){

                MethodUtils.safeSetValue(balance,mdt.balance + mdt.currentChosenMultipliyerValue*Math.abs(tempLeftTime)*miningBaseValue);
                Log.d("BALANCE",""+mdt.balance + mdt.currentChosenMultipliyerValue*Math.abs(tempLeftTime));
                Log.d("BALANCE",""+mdt.currentChosenMultipliyerValue);
                Log.d("BALANCE",""+mdt.balance);
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
                MethodUtils.safeSetValue(balance,mdt.balance + mdt.currentChosenMultipliyerValue*miningBaseValue*(mdt.estimated_end_time/1000L));
                MethodUtils.safeSetValue(currentChosenMultipliyer,MULTIPLYER_ENUM.getEnumValue(1));
                MethodUtils.safeSetValue(isActive,false);
            }
        }
        else{
            MethodUtils.safeSetValue(balance,mdt.balance);
        }
        if(!MainViewModel.userHasBeenJustCreated){
            if(Boolean.FALSE.equals(canWithdraw.getValue())||mdt.cooldown_estimated_end_time==0){
                long temp  = (serverTime - mdt.exit_time);
                Log.d("Repo111","fired off" +temp);
                Log.d("Repo111","fired off" +mdt.exit_time);
                Log.d("Repo111","fired off" +serverTime);
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
}
