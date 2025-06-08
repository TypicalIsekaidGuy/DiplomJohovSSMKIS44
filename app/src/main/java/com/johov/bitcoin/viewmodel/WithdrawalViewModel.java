package com.johov.bitcoin.viewmodel;


import static com.johov.bitcoin.data.repository.MainDataRepository.didShowRateWindow;
import static com.johov.bitcoin.data.repository.MainDataRepository.fullTimerDuration;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.johov.bitcoin.MainActivity;
import com.johov.bitcoin.data.db.AppDatabase;
import com.johov.bitcoin.data.db.maindata.MainDataTable;
import com.johov.bitcoin.data.db.transactiondata.TransactionTable;
import com.johov.bitcoin.data.model.enums.CONNECTION_ERROR_ENUM;
import com.johov.bitcoin.data.model.enums.MULTIPLYER_ENUM;
import com.johov.bitcoin.data.model.Transaction;
import com.johov.bitcoin.data.repository.MainDataRepository;
import com.johov.bitcoin.utils.MethodUtils;
import com.johov.bitcoin.utils.StringUtils;

@HiltViewModel
public class WithdrawalViewModel extends ViewModel {
    public MainDataRepository mainDataRepository;
    Context context;

    Retrofit retrofit;

    AppDatabase db;

    //main vars
    public MutableLiveData<MULTIPLYER_ENUM> currentChosenMultipliyer = new MutableLiveData<>(MULTIPLYER_ENUM.MULTYPLIER_1x);

    public MutableLiveData<Float> balance = new MutableLiveData<>(0f);
    public MutableLiveData<Boolean> isMining = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> canWithdraw = new MutableLiveData<>(false);
    public MutableLiveData<List<Transaction>> transactionList = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<Long> cooldownmillisUntilFinishedLiveData = new MutableLiveData<>(864000000L);

    public MutableLiveData<Long> millisUntilFinishedLiveData = new MutableLiveData<>(864000000L);

    //presentation vars
    public MutableLiveData<Float> progress = new MutableLiveData<Float>(0f);
    public MutableLiveData<Boolean> showPopupRate = new MutableLiveData<>(false);

    @Inject
    WithdrawalViewModel(@ApplicationContext Context context,MainDataRepository mainDataRepository,AppDatabase db){
        this.mainDataRepository = mainDataRepository;
        this.context = context;
        this.retrofit = retrofit;
        this.db = db;

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> getTransaction(),1000);
        setUpObservers();
    }

    private void setUpObservers(){
        mainDataRepository.currentChosenMultipliyer.observeForever(newValue -> currentChosenMultipliyer.postValue(newValue));

        mainDataRepository.balance.observeForever(newValue -> balance.postValue(newValue));

        mainDataRepository.isActive.observeForever(newValue -> isMining.postValue(newValue));

        mainDataRepository.canWithdraw.observeForever(newValue -> canWithdraw.postValue(newValue));

        mainDataRepository.transactionList.observeForever(newValue -> transactionList.postValue(newValue));

        mainDataRepository.millisUntilFinishedLiveData.observeForever(newValue -> {
            if(newValue!=0){

                updateProgress(newValue);

                if(newValue<=500L){
                    onTimerEnd();
                }

                millisUntilFinishedLiveData.postValue(newValue);
            }
        });

        mainDataRepository.cooldownmillisUntilFinishedLiveData.observeForever(newValue -> cooldownmillisUntilFinishedLiveData.postValue(newValue));
    }

    //presentation functions
    public void showSnackBar(String text, Boolean hasSucceded){
        MainActivity.Companion.setHasSucceded(hasSucceded);
        if(Objects.equals(MainActivity.Companion.getSnackBarText().getValue(), text)){
            MethodUtils.safeSetValue(MainActivity.Companion.getSnackBarText(),text+" ");
        }
        else{
            MethodUtils.safeSetValue(MainActivity.Companion.getSnackBarText(),text);
        }
    }
    public void updateProgress(Long estimatedTime){
        progress.setValue( ((float)estimatedTime/fullTimerDuration));
    }
    @SuppressLint("DefaultLocale")
    private String getTimeCooldown(MutableLiveData<Long> cooldownmillisUntilFinishedLiveData){
        if(cooldownmillisUntilFinishedLiveData.getValue()!=null){
            int hours = Math.toIntExact(((cooldownmillisUntilFinishedLiveData.getValue() / 1000) / 3600));
            int minutes = Math.toIntExact((((cooldownmillisUntilFinishedLiveData.getValue() / 1000) / 60) % 60));
            int seconds = Math.toIntExact(((cooldownmillisUntilFinishedLiveData.getValue() / 1000) % 60));
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return "";
    }

    //onClick functions
    public void withdrawalOnClick(){

                Log.d("usercreated","withdraw "+canWithdraw.getValue());
                if(canWithdraw.getValue()!=null&&canWithdraw.getValue()){
                    Float bucks = mainDataRepository.getBalanceForWithdrawal();
                    if(bucks!=null){

                        tryToShowRateWindow(bucks);

                        createTransaction(bucks);

                        mainDataRepository.resetBalance();
                        mainDataRepository.refreshCooldown(mainDataRepository.widthdrawalDelay);

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(() -> getTransaction(),1000);

                        saveUserData();

                    }
                    else{
                        showSnackBar("You can't withdraw with empty balance",false);
                    }

                }
                else{
                    Log.d("REPO!!","withdraw "+mainDataRepository.cooldownmillisUntilFinishedLiveData.getValue());
                    showSnackBar("The withdrawal will be available after "+getTimeCooldown(cooldownmillisUntilFinishedLiveData),false);
                }

        Log.d("NETWORKTAG",""+mainDataRepository.urlGooglePlay.getValue());
        Log.d("NETWORKTAG",""+mainDataRepository.minValue);
        Log.d("NETWORKTAG",""+mainDataRepository.maxValue);
    }

    public void createTransaction(float bucks){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            db.transactionDataDao().insert(new TransactionTable(bucks));
        });
    }

    public void copyCodeOnClick(String text){

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        showSnackBar("Code copied", true);

    }

    public void googlePlayCodeOnClick(){
        if(mainDataRepository.urlGooglePlay.getValue()!=null){
            MainActivity.Companion.getLink().setValue(mainDataRepository.urlGooglePlay.getValue());
        }
    }

    //helper functions

    public void getTransaction() {

        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<TransactionTable> transactions = db.transactionDataDao().getAll();


            Log.d("networkssss","found list");
            List<Transaction> replaceList = new ArrayList<>();
            for (TransactionTable trans : transactions) {
                replaceList.add(new Transaction(trans));
            }

            Collections.sort(replaceList);
            Collections.reverse(replaceList);
            mainDataRepository.transactionList.postValue(replaceList);
            transactionList.postValue(replaceList);
            Log.d("networkssss",""+transactionList.getValue().size());
            Log.d("networkssss",""+mainDataRepository.transactionList.getValue().size());



        });

    }

    private void tryToShowRateWindow(float sum){//new
        Log.d("Withdrawal",""+sum);
        Log.d("Withdrawal",""+mainDataRepository.withdrawalRateTriggerSum/mainDataRepository.convertValueToOneUsdt);
        Log.d("Withdrawal",""+didShowRateWindow.getValue());
        if(mainDataRepository.withdrawalRateTriggerSum/mainDataRepository.convertValueToOneUsdt<sum && Boolean.FALSE.equals(didShowRateWindow.getValue())){
            MethodUtils.safeSetValue(showPopupRate,true);
        }
    }

    public void dismissRateScreenAndNavigate(){//new
        MethodUtils.safeSetValue(didShowRateWindow,true);
        MethodUtils.safeSetValue(showPopupRate,false);
        saveUserData();

        MainActivity.Companion.getLink().setValue("https://www.rustore.ru/catalog/app/"+MainActivity.Companion.getSource());
    }

    public void dismissRateScreen(){//new
        MethodUtils.safeSetValue(showPopupRate,false);
    }

    private void saveUserData(){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            MainDataTable mdt = new MainDataTable(mainDataRepository,System.currentTimeMillis());
            db.mainDataDao().update(mdt);
        });
    }


    //util functions
    public boolean isOnline(){
        return MainActivity.Companion.isOnline((Context) context);
    }
}
