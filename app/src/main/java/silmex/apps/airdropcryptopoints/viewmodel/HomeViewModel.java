package silmex.apps.airdropcryptopoints.viewmodel;

import static silmex.apps.airdropcryptopoints.data.repository.MainDataRepository.fullTimerDuration;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import silmex.apps.airdropcryptopoints.MainActivity;
import silmex.apps.airdropcryptopoints.data.model.MULTIPLYER_ENUM;
import silmex.apps.airdropcryptopoints.data.repository.MainDataRepository;
import silmex.apps.airdropcryptopoints.ui.view.composables.Coin;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    public MainDataRepository mainDataRepository;
    Context context;

    //main vars
    public MutableLiveData<MULTIPLYER_ENUM> currentChosenMultipliyer = new MutableLiveData<>(MULTIPLYER_ENUM.MULTYPLIER_1x);
    public MutableLiveData<Float> balance = new MutableLiveData<>(0f);
    public MutableLiveData<Boolean> isMining = new MutableLiveData<>(false);

    //presentation vars
    public String defaultTimerText = "00:00:00";
    public MutableLiveData<String> timerText = new MutableLiveData<String>(defaultTimerText);
    public MutableLiveData<Float> progress = new MutableLiveData<Float>(0f);
    public MutableLiveData<List<Coin>> coins = new MutableLiveData<>(new ArrayList<>());

    @Inject
    HomeViewModel(@ApplicationContext Context context,MainDataRepository mainDataRepository){
        this.mainDataRepository = mainDataRepository;
        this.context = context;
        setUpObservers();
        setUpPresentationVars();
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
                if(!newValue){
                    timerText.setValue(defaultTimerText);
                }
            }
        });

        mainDataRepository.millisUntilFinishedLiveData.observeForever(new Observer<Long>() {
            @Override
            public void onChanged(Long newValue) {



                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (newValue / 3600000) % 24;
                long min = (newValue / 60000) % 60;
                long sec = (newValue / 1000) % 60;
                timerText.setValue(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));


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
    private void setUpPresentationVars(){
        for(int i = 0; i<500; i++){
            coins.getValue().add(null);
        }
    }

    //presentation functions
    public void onTimerEnd(){
        timerText.setValue(defaultTimerText);
        progress.setValue(0f);
    }
    public void updateProgress(Long estimatedTime){
        progress.setValue( ((float)estimatedTime/fullTimerDuration));
    }
    public void removeCoin(Integer id) {
        List<Coin> currentCoins = coins.getValue();
        if (currentCoins != null) {
            currentCoins.set(id,null);
            coins.setValue(currentCoins);
        }
    }
    private void addCoin(Integer count){
        if(coins.getValue().size()==500){
            for(int i = 0; i<count; i++){
                Integer j = 0;
                List<Coin> coinList = coins.getValue();
                while(coins.getValue().get(499) ==null){
                    if(coins.getValue().get(j)==null){
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
    public void upgradeWheelClick(){
        upgradeWheel();
    }
    public void claimClick(){
        mainDataRepository.setTimer();
    }
    public void upgradeWheel(){

        switch (Objects.requireNonNull(currentChosenMultipliyer.getValue())){
            case MULTYPLIER_1x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_2x);
            }
            break;
            case MULTYPLIER_2x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_3x);
            }
            break;
            case MULTYPLIER_3x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_5x);
            }
            break;
            case MULTYPLIER_5x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_8x);
            }
            break;
            case MULTYPLIER_8x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_13x);
            }
            break;
            case MULTYPLIER_13x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_21x);
            }
            break;
            case MULTYPLIER_21x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_34x);
            }
            break;
            case MULTYPLIER_34x:{
                mainDataRepository.currentChosenMultipliyer.setValue(MULTIPLYER_ENUM.MULTYPLIER_55x);
            }
            break;
            case MULTYPLIER_55x:{

            }
            break;
        }
    }
}
