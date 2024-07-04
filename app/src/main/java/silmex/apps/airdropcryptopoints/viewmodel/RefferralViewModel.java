package silmex.apps.airdropcryptopoints.viewmodel;

import static androidx.core.content.ContextCompat.startActivity;

import static silmex.apps.airdropcryptopoints.data.repository.MainDataRepository.fullTimerDuration;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
public class RefferralViewModel extends ViewModel {
    public MainDataRepository mainDataRepository;

    @SuppressLint("StaticFieldLeak")
    Context context;

    //main vars
    public MutableLiveData<MULTIPLYER_ENUM> currentChosenMultipliyer = new MutableLiveData<>(MULTIPLYER_ENUM.MULTYPLIER_1x);
    public MutableLiveData<Float> balance = new MutableLiveData<>(0f);
    public MutableLiveData<Boolean> isMining = new MutableLiveData<>(false);

    //presentation vars
    public MutableLiveData<List<Coin>> coins = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<String> textValue = new MutableLiveData<>("");
    public Integer limitOfCode = 10;
    public MutableLiveData<Float> progress = new MutableLiveData<Float>(0f);

    @Inject
    RefferralViewModel(@ApplicationContext Context context, MainDataRepository mainDataRepository){
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

    //onClick functions
    public void shareCodeOnClick(){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mainDataRepository.referralCode.toString());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);

        MainActivity.Companion.getShareIntent().setValue(shareIntent);

    }

    public void copyCodeOnClick(){

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", mainDataRepository.referralCode.toString());
        clipboard.setPrimaryClip(clip);
        showToast("Referral code copied", true);

    }

    public void getBonusFromCodeOnClick(){

        if(textValue.getValue().length()==limitOfCode){
            showToast("Bonus added", true);
            textValue.setValue("");
        }
        else{
            showToast("Wrong Referral code", false);
        }

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
}
