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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    public MutableLiveData<Long> leftTime = new MutableLiveData<Long>(0L);
    public MutableLiveData<Float> progress = new MutableLiveData<Float>(0f);

    @Inject
    HomeViewModel(@ApplicationContext Context context,MainDataRepository mainDataRepository){
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

        mainDataRepository.millisUntilFinishedLiveData.observeForever(new Observer<Long>() {
            @Override
            public void onChanged(Long newValue) {
                if(newValue!=0) {

                    leftTime.setValue(newValue);

                    updateProgress(newValue);

                    if (newValue <= 500L) {
                        onTimerEnd();
                    }
                }
            }
        });
    }

    //presentation functions
    public void onTimerEnd(){
        progress.setValue(0f);
    }

    public void updateProgress(Long estimatedTime){
        progress.setValue( ((float)estimatedTime/fullTimerDuration));
    }



    //onClick functions
    public void upgradeWheelClick(){
        upgradeWheel();
    }
    public void claimClick(){
        mainDataRepository.setTimer();
        mainDataRepository.claimBalance();
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
