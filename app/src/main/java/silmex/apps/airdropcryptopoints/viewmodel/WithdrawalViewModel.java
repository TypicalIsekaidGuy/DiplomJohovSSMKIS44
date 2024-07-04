package silmex.apps.airdropcryptopoints.viewmodel;


import static silmex.apps.airdropcryptopoints.data.repository.MainDataRepository.fullTimerDuration;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlin.random.RandomKt;
import silmex.apps.airdropcryptopoints.MainActivity;
import silmex.apps.airdropcryptopoints.data.model.MULTIPLYER_ENUM;
import silmex.apps.airdropcryptopoints.data.model.Transaction;
import silmex.apps.airdropcryptopoints.data.repository.MainDataRepository;
import silmex.apps.airdropcryptopoints.ui.view.composables.Coin;

@HiltViewModel
public class WithdrawalViewModel extends ViewModel {
    public MainDataRepository mainDataRepository;
    Context context;

    //main vars
    public MutableLiveData<MULTIPLYER_ENUM> currentChosenMultipliyer = new MutableLiveData<>(MULTIPLYER_ENUM.MULTYPLIER_1x);
    public MutableLiveData<Float> balance = new MutableLiveData<>(0f);
    public MutableLiveData<Boolean> isMining = new MutableLiveData<>(false);
    public MutableLiveData<List<Transaction>> transactionList = new MutableLiveData<>(new ArrayList<>());

    //presentation vars
    public MutableLiveData<List<Coin>> coins = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<Float> progress = new MutableLiveData<Float>(0f);

    @Inject
    WithdrawalViewModel(@ApplicationContext Context context,MainDataRepository mainDataRepository){
        this.mainDataRepository = mainDataRepository;
        this.context = context;
        setUpObservers();
    }
    private void setUpObservers(){
        mainDataRepository.currentChosenMultipliyer.observeForever(new Observer<MULTIPLYER_ENUM>() {
            @Override
            public void onChanged(MULTIPLYER_ENUM newValue) {

                currentChosenMultipliyer.postValue(newValue);
            }
        });
        mainDataRepository.balance.observeForever(new Observer<Float>() {
            @Override
            public void onChanged(Float newValue) {

                balance.postValue(newValue);
            }
        });
        mainDataRepository.isActive.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean newValue) {

                isMining.postValue(newValue);
            }
        });
        mainDataRepository.transactionList.observeForever(new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> newValue) {

                transactionList.postValue(newValue);
            }
        });

        mainDataRepository.millisUntilFinishedLiveData.observeForever(new Observer<Long>() {
            @Override
            public void onChanged(Long newValue) {

                // For animation of coins
                if(Boolean.FALSE.equals(MainActivity.Companion.isOnPaused().getValue())){
                    addCoin(currentChosenMultipliyer.getValue().getValue());
                }

                updateProgress(newValue);

                if(newValue<=500L){
                    onTimerEnd();
                }
            }
        });
    }


    //presentation functions
    public void onTimerEnd(){

    }
    public void removeCoin(Integer id) {
        List<Coin> currentCoins = coins.getValue();
        if (currentCoins != null) {
            Log.d("VIEWMODElend",id.toString());
            currentCoins.set(id,null);
            coins.setValue(currentCoins);
        }
    }
    public void showToast(String text,Boolean hasSucceded){
        MainActivity.Companion.setHasSucceded(hasSucceded);
        MainActivity.Companion.getToastText().setValue(text);
    }
    public void updateProgress(Long estimatedTime){
        progress.setValue( ((float)estimatedTime/fullTimerDuration));
    }
    private void addCoin(Integer count){
        if(coins.getValue().size()==500){
            for(int i = 0; i<count; i++){
                Integer j = 0;
                List<Coin> coinList = coins.getValue();
                while(coins.getValue().get(499) ==null){
                    if(coins.getValue().get(j)==null){
                        Log.d("VIEWMODEl",j.toString());
                        Random random = new Random();
                        coinList.set(j,new Coin(j,random.nextFloat()+ 0.5f,random.nextFloat()/1.5f,random.nextFloat()/3,random.nextFloat()/3+ 0.5f,random.nextFloat()/3 + 0.5f));
                        coins.setValue(coinList);
                        break;
                    }
                    j++;
                }
            }
        }
    }



    //onClick functions
    public void withdrawalOnClick(){
        if(!isCurrentDayInWithdrawDates(mainDataRepository.withdrawDates)){
            showToast("Today is not the day of withdrawal",false);
        }
        else {
            mainDataRepository.addTransaction();
            Log.d("VIEWMODELTESTS",""+mainDataRepository.transactionList.getValue().size());
            Log.d("VIEWMODELTESTS",""+transactionList.getValue().size());
        }
    }
    public void copyCodeOnClick(String text){

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        showToast("Code copied", true);

    }

    //help functions
    public boolean isCurrentDayInWithdrawDates(List<Date> withdrawDates) {
        //TODO remove back
/*        Calendar currentCalendar = Calendar.getInstance();
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

        for (Date date : withdrawDates) {
            Calendar withdrawCalendar = Calendar.getInstance();
            withdrawCalendar.setTime(date);
            int withdrawDay = withdrawCalendar.get(Calendar.DAY_OF_MONTH);

            if (currentDay == withdrawDay) {
                return true;
            }
        }
        return false;*/
        return RandomKt.Random(System.nanoTime()).nextBoolean();
    }
}
