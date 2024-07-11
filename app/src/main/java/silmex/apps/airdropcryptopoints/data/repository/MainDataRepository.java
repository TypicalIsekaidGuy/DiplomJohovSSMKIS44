package silmex.apps.airdropcryptopoints.data.repository;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.stream.Collectors;

import silmex.apps.airdropcryptopoints.data.db.maindata.MainDataTable;
import silmex.apps.airdropcryptopoints.data.model.MULTIPLYER_ENUM;
import silmex.apps.airdropcryptopoints.data.model.Transaction;
import silmex.apps.airdropcryptopoints.utils.ConvertUtils;
import silmex.apps.airdropcryptopoints.utils.IntegerUtils;
import silmex.apps.airdropcryptopoints.utils.MethodUtils;
import silmex.apps.airdropcryptopoints.utils.StringUtils;
import silmex.apps.airdropcryptopoints.utils.TagUtils;

public class MainDataRepository {

    //vars for configing app
    public static Integer random_for_save = null;
    public static String unityID = "5457496";
    public static String unityBlock = "Rewarded_Android";

    public static String geteDeviceIdentifier() {
        String uniqueDevicePseudoID = "97" +            Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +            Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +            Build.HOST.length() % 10 +
                Build.ID.length() % 10 +            Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +            Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +            Build.TYPE.length() % 10 +
                Build.USER.length() % 10+ random_for_save;
        String serial = Build.getRadioVersion();
        Log.d("workkk", new UUID(uniqueDevicePseudoID.hashCode(), serial.hashCode()).toString());
        return new UUID(uniqueDevicePseudoID.hashCode(), serial.hashCode()).toString();
    }

    //main data vars
    public MutableLiveData<Boolean> didShowLearning = new MutableLiveData<Boolean>(false);
    public MutableLiveData<Float> balance = new MutableLiveData<Float>(0F);
    public MutableLiveData<MULTIPLYER_ENUM> currentChosenMultipliyer = new MutableLiveData<MULTIPLYER_ENUM>(MULTIPLYER_ENUM.MULTYPLIER_1x);
    public MutableLiveData<Boolean> isActive = new MutableLiveData<>(false);
    public Date serverTime = null;

    //referral data vars
    public MutableLiveData<Integer> referralCode = new MutableLiveData<Integer>(Math.abs(IntegerUtils.generateRandomInteger()));
    public String enteredCode = "";
    public Integer yourRefferalBonus = 1000000;
    public Integer otherRefferalBonus = 1000000;
    public Integer referals = 0;
    public boolean hasNotEnteredCode(){
        return enteredCode.isEmpty();
    }

    //withdrawal data vars
    public final List<Date> withdrawDates = new ArrayList<Date>() {{
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        add(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        add(calendar.getTime());
    }};
    public MutableLiveData<List<Transaction>> transactionList = new MutableLiveData<List<Transaction>>(new ArrayList<Transaction>());
    public MutableLiveData<String> urlGooglePlay = new MutableLiveData<>("https://t.me/finsignal");
    public float minValue =0.01f;
    public float maxValue =49.74f;
    public int convertValueToOneUsdt =198000;
    public long widthdrawalDelay =86400000;

    //presentation vars
    public MutableLiveData<String> learningText = new MutableLiveData<String>("Invite friends and get \n1 000 000 Crypto Points");
    public MutableLiveData<String> refferalText1 = new MutableLiveData<String>("Invite friends and receive 1 000 000 crypto points, each invited friend will receive 1 000 000 crypto points");
    public MutableLiveData<String> refferealText2 = new MutableLiveData<String>("Enter my referral code #CODE# and receive 1 000 000 crypto points in the AirDrop Crypto Points application - #LINK#");



    //timer vars
    public CountDownTimer mainTimer;
    public static final long fullTimerDuration = 14400000/60;//TODO change to 14400000
    public MutableLiveData<Long> millisUntilFinishedLiveData = new MutableLiveData<>(0L);
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
/*                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(millisUntilFinished<=500L){
                                turnOffTimer();
                            }
                            MethodUtils.safeSetValue(balance,Objects.requireNonNull(balance.getValue())+ Objects.requireNonNull(currentChosenMultipliyer.getValue()).getValue());
                            MethodUtils.safeSetValue(millisUntilFinishedLiveData,millisUntilFinished);
                        }
                    });*/
                }
                public void onFinish() {

                    turnOffTimer();
/*                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            turnOffTimer();
                        }
                    });*/
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

    public void getRefferalYourCodeBonus(Integer diff){
        balance.postValue(balance.getValue()+(yourRefferalBonus*diff));
    }
    public void getRefferalOtherCodeBonus(Integer diff){
        balance.postValue(balance.getValue()+(otherRefferalBonus*diff));
    }
    public void claimBalance(){
        MethodUtils.safeSetValue(currentChosenMultipliyer,MULTIPLYER_ENUM.MULTYPLIER_1x);
    }
    public void addTransaction(){
        List<Transaction> transactions = transactionList.getValue();
        transactions.add(new Transaction(ConvertUtils.stringToDate("2024-04-10 11:23:34"),114.32F,"","","","231ssывыфв",0));
        Collections.sort(transactions);

        MethodUtils.safeSetValue(transactionList,transactions);
    }

    //constructor
    public MainDataRepository(){
        List<Transaction> transactions = transactionList.getValue();
        transactions.add(new Transaction(ConvertUtils.stringToDate("2024-03-10 11:23:32"),114.32F,"","","","231sasdasa",0));
        transactions.add(new Transaction(ConvertUtils.stringToDate("2024-04-10 11:23:31"),114.32F,"","","","sadsdasd",1));
        transactions.add(new Transaction(ConvertUtils.stringToDate("2024-05-10 11:23:30"),114.32F,"","","","37ygjhkhtu8",2));
        Collections.sort(transactions);
        MethodUtils.safeSetValue(transactionList,transactions);
    }

    public void updateMainDataRepo(MainDataTable mdt, Long currentTime){
        random_for_save = mdt.random_save_id;
        Log.d(TagUtils.MAINVIEWMODELTAG,"Worked db");

        MethodUtils.safeSetValue(didShowLearning,mdt.didShowLearning);
        MethodUtils.safeSetValue(currentChosenMultipliyer,MULTIPLYER_ENUM.getEnumValue(mdt.currentChosenMultipliyerValue));
        MethodUtils.safeSetValue(isActive,mdt.isActive);

/*        if(mdt.isActive){
            tempLeftTime  = (currentTime - mdt.exit_time)/1000L;
            long timerDiffernce = (tempLeftTime - mdt.estimated_end_time/1000L);

            Log.d("Values",""+mdt.exit_time);
            Log.d("Values",""+currentTime);
            Log.d("Values",""+tempLeftTime);
            Log.d("Values",""+mdt.estimated_end_time/1000L);
            Log.d("Values",""+timerDiffernce);

            if(timerDiffernce<=0){

                MethodUtils.safeSetValue(balance,mdt.balance + mdt.currentChosenMultipliyerValue*Math.abs(tempLeftTime));
                MethodUtils.safeSetValue(millisUntilFinishedLiveData,Math.abs(timerDiffernce*1000L));

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        updateTimer(Math.abs(timerDiffernce*1000L));
                    }
                });
            }
            else{
                MethodUtils.safeSetValue(balance,mdt.balance + mdt.currentChosenMultipliyerValue*(mdt.estimated_end_time/1000L));
            }
        }
        else{
            MethodUtils.safeSetValue(balance,mdt.balance);
        }*/
        MethodUtils.safeSetValue(balance,mdt.balance);
    }
    public void updateMainDataRepo(MainDataTable mdt, Date serverTime){
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
    }
}
