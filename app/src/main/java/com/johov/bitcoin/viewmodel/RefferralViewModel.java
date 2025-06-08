package com.johov.bitcoin.viewmodel;

import static com.johov.bitcoin.data.repository.MainDataRepository.fullTimerDuration;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Retrofit;
import com.johov.bitcoin.MainActivity;
import com.johov.bitcoin.data.db.AppDatabase;
import com.johov.bitcoin.data.db.maindata.MainDataTable;
import com.johov.bitcoin.data.model.enums.CONNECTION_ERROR_ENUM;
import com.johov.bitcoin.data.repository.MainDataRepository;
import com.johov.bitcoin.utils.MethodUtils;
import com.johov.bitcoin.utils.StringUtils;

@HiltViewModel
public class RefferralViewModel extends ViewModel {
    public MainDataRepository mainDataRepository;

    @SuppressLint("StaticFieldLeak")
    Context context;

    Retrofit retrofit;

    AppDatabase db;

    //main vars
    public MutableLiveData<Float> balance = new MutableLiveData<>(0f);
    public MutableLiveData<String> refCode = new MutableLiveData<>(StringUtils.generateDeviceIdentifier().substring(StringUtils.generateDeviceIdentifier().length() - 10));
    public MutableLiveData<Boolean> isMining = new MutableLiveData<>(false);

    //presentation vars
    public MutableLiveData<String> textValue = new MutableLiveData<>("");
    public MutableLiveData<String> refferralTextValue1 = new MutableLiveData<>("");
    public MutableLiveData<String> refferralTextValue2 = new MutableLiveData<>("");
    public Integer limitOfCode = 10;
    public MutableLiveData<Float> progress = new MutableLiveData<Float>(0f);

    @Inject
    RefferralViewModel(@ApplicationContext Context context, MainDataRepository mainDataRepository,  AppDatabase db){
        this.mainDataRepository = mainDataRepository;
        this.context = context;
        this.retrofit = retrofit;
        this.db = db;

        setUpObservers();
    }

    //main functions
    private void setUpObservers(){
        mainDataRepository.balance.observeForever(newValue -> balance.postValue(newValue));
        mainDataRepository.isActive.observeForever(newValue -> isMining.postValue(newValue));
        mainDataRepository.referralCode.observeForever(newValue -> {
            String target = refferralTextValue2.getValue();
            if(target!=null){
                target = target.replace(Objects.requireNonNull(refCode.getValue()).toString(), Objects.requireNonNull(newValue).toString());

                refferralTextValue2.postValue(target);
            }
            Log.d("REFFERAL1","WORKED" + refferralTextValue2.getValue());
            Log.d("REFFERAL1","WORKED target" + target);
            Log.d("REFFERAL1","WORKED" + Objects.requireNonNull(refCode.getValue()).toString());
            refCode.postValue(newValue.substring(newValue.length() - 10));
        });

        mainDataRepository.refferalText1.observeForever(
                newValue ->{
                    String target = newValue.replace("#CODE#", Objects.requireNonNull(refCode).toString());
                    refferralTextValue1.postValue(target);
                }
        );

        mainDataRepository.refferealText2.observeForever(newValue -> {
            if(refCode.getValue()!=null&&refCode.getValue()!=null){
                String target = newValue.replace("#CODE#",refCode.getValue().toString());
                target = target.replace("#LINK#","https://play.google.com/store/apps/details?id="+MainActivity.Companion.getSource());

                refferralTextValue2.postValue(target);
            }
            else if(mainDataRepository.referralCode.getValue()!=null&&mainDataRepository.referralCode.getValue()!=null){
                String target = newValue.replace("#CODE#",mainDataRepository.referralCode.getValue().toString());
                target = target.replace("#LINK#","https://play.google.com/store/apps/details?id="+MainActivity.Companion.getSource());

                refferralTextValue2.postValue(target);
            }
            Log.d("REFFERAL2","WORKED" + refferralTextValue2.getValue());
            Log.d("REFFERAL3","WORKED" + refCode.getValue());
            Log.d("REFFERAL4","WORKED" + mainDataRepository.referralCode.getValue());
        });

        mainDataRepository.millisUntilFinishedLiveData.observeForever(new Observer<Long>() {
            @Override
            public void onChanged(Long newValue) {
                if(newValue!=0) {

                    updateProgress(newValue);
                }
            }
        });
    }


    //onClick functions
    public void shareCodeOnClick(){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, refferralTextValue2.getValue());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);

        MainActivity.Companion.getShareIntent().setValue(shareIntent);
    }


    public void copyCodeOnClick(){

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", refCode.getValue().toString());
        clipboard.setPrimaryClip(clip);
        showSnackBar("Referral code copied", true);

    }

    public void getBonusFromCodeOnClick(){

        if(isOnline()){
            showSnackBar("Very sorry referral codes are not yet supported", false);

        }
        else{
            MainViewModel.throwConnectionError(CONNECTION_ERROR_ENUM.GET_REFCODE_BONUS_CLICK);
        }

    }

    //helper functions
    private void getOtherCodeBonus(boolean success){
        if (success) {
            mainDataRepository.getRefferalYourCodeBonus(1);
            saveUserData();
            showToast("Bonus added");
            MethodUtils.safeSetValue(textValue,"");
        } else {
            showSnackBar("Wrong referral code", false);
        }
    }


    //presentation functions
    private void showSnackBar(String text, Boolean hasSucceded){
        MainActivity.Companion.setHasSucceded(hasSucceded);
        if(Objects.equals(MainActivity.Companion.getSnackBarText().getValue(), text)){
            MethodUtils.safeSetValue(MainActivity.Companion.getSnackBarText(),text+" ");
        }
        else{
            MethodUtils.safeSetValue(MainActivity.Companion.getSnackBarText(),text);
        }
    }
    private void showToast(String text){
        MethodUtils.safeSetValue(MainActivity.Companion.getToastText(),text);
    }
    private void updateProgress(Long estimatedTime){
        progress.setValue( ((float)estimatedTime/fullTimerDuration));
    }

    //util functions
    private boolean isOnline(){
        return MainActivity.Companion.isOnline(context);
    }
    private void saveUserData(){

        AppDatabase.databaseWriteExecutor.execute(() -> {
            MainDataTable mdt = new MainDataTable(mainDataRepository, System.currentTimeMillis());
            db.mainDataDao().update(mdt);
            Log.d("Balance",""+System.currentTimeMillis());
        });

    }
}
