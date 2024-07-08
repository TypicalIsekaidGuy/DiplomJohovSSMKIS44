package silmex.apps.airdropcryptopoints.viewmodel;

import static silmex.apps.airdropcryptopoints.data.repository.MainDataRepository.fullTimerDuration;

import android.annotation.SuppressLint;
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

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import silmex.apps.airdropcryptopoints.MainActivity;
import silmex.apps.airdropcryptopoints.data.model.MULTIPLYER_ENUM;
import silmex.apps.airdropcryptopoints.data.repository.MainDataRepository;
import silmex.apps.airdropcryptopoints.ui.view.composables.Coin;

@HiltViewModel
public class MainViewModel extends ViewModel {
    public MainDataRepository mainDataRepository;

    @SuppressLint("StaticFieldLeak")
    Context context;

    //main vars
    public MutableLiveData<MULTIPLYER_ENUM> currentChosenMultipliyer = new MutableLiveData<>(MULTIPLYER_ENUM.MULTYPLIER_1x);
    //presentation vars
    public MutableLiveData<List<Coin>> coins = new MutableLiveData<>(new ArrayList<>());
    public CountDownTimer coinTimer = null;

    @Inject
    MainViewModel(@ApplicationContext Context context, MainDataRepository mainDataRepository){
        this.mainDataRepository = mainDataRepository;
        this.context = context;
        setUpObservers();
        setUpPresentationVars();
    }
    private void setUpObservers(){
        mainDataRepository.currentChosenMultipliyer.observeForever(new Observer<MULTIPLYER_ENUM>() {
            @Override
            public void onChanged(MULTIPLYER_ENUM newValue) {
                updateTimer(newValue);
            }
        });
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

}
