package silmex.apps.airdropcryptopoints.data.repository;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.CountDownTimer;
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

import silmex.apps.airdropcryptopoints.data.model.MULTIPLYER_ENUM;
import silmex.apps.airdropcryptopoints.data.model.Transaction;
import silmex.apps.airdropcryptopoints.utils.ConvertUtils;
import silmex.apps.airdropcryptopoints.utils.IntegerUtils;
import silmex.apps.airdropcryptopoints.utils.StringUtils;

public class MainDataRepository {

    //vars for configing app
    public static Integer random_for_save = null;
    public boolean didShowLearning = false;

    //main data vars
    public MutableLiveData<Float> balance = new MutableLiveData<Float>(0F);
    public MutableLiveData<MULTIPLYER_ENUM> currentChosenMultipliyer = new MutableLiveData<MULTIPLYER_ENUM>(MULTIPLYER_ENUM.MULTYPLIER_1x);

    //referral data vars
    public Integer referralCode = Math.abs(IntegerUtils.generateRandomInteger());

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


    //timer vars
    public CountDownTimer mainTimer;
    public static final long fullTimerDuration = 14400000/60;//TODO change to 14400000
    public MutableLiveData<Long> millisUntilFinishedLiveData = new MutableLiveData<>(0L);
    public MutableLiveData<Boolean> isActive = new MutableLiveData<>(false);

    //main vars setters
    //timer setters
    public void setTimer(){
        if(Boolean.FALSE.equals(isActive.getValue())&&mainTimer==null){

            isActive.setValue(true);
            mainTimer = new CountDownTimer(fullTimerDuration, 1000) {
                public void onTick(long millisUntilFinished) {
                    if(millisUntilFinished<=500L){
                        turnOffTimer();
                    }
                    balance.setValue(Objects.requireNonNull(balance.getValue())+ Objects.requireNonNull(currentChosenMultipliyer.getValue()).getValue());
                    millisUntilFinishedLiveData.setValue(millisUntilFinished);
                }
                public void onFinish() {
                    turnOffTimer();
                }
            }.start();
        }
    }
    public void turnOffTimer(){
        if(mainTimer!=null){

            isActive.setValue(false);

            mainTimer.cancel();
            mainTimer = null;
        }
    }
    public void addTransaction(){
        List<Transaction> transactions = transactionList.getValue();
        transactions.add(new Transaction(ConvertUtils.stringToDate("2024-04-1011:23:34"),114.32F,"","","","231ssывыфв",0));
        Collections.sort(transactions);
        transactionList.setValue(transactions);
    }

    //constructor
    public MainDataRepository(){
        List<Transaction> transactions = transactionList.getValue();
        transactions.add(new Transaction(ConvertUtils.stringToDate("2024-03-1011:23:32"),114.32F,"","","","231sasdasa",0));
        transactions.add(new Transaction(ConvertUtils.stringToDate("2024-04-1011:23:31"),114.32F,"","","","sadsdasd",1));
        transactions.add(new Transaction(ConvertUtils.stringToDate("2024-05-1011:23:30"),114.32F,"","","","37ygjhkhtu8",2));
        Collections.sort(transactions);
        transactionList.setValue(transactions);
    }
}
