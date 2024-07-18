package silmex.apps.airdropcryptopoints.viewmodel;

import static androidx.core.content.ContextCompat.startActivity;
import static silmex.apps.airdropcryptopoints.data.repository.MainDataRepository.fullTimerDuration;
import static silmex.apps.airdropcryptopoints.viewmodel.MainViewModel.log;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import silmex.apps.airdropcryptopoints.MainActivity;
import silmex.apps.airdropcryptopoints.R;
import silmex.apps.airdropcryptopoints.data.db.AppDatabase;
import silmex.apps.airdropcryptopoints.data.db.maindata.MainDataTable;
import silmex.apps.airdropcryptopoints.data.model.CONNECTION_ERROR_ENUM;
import silmex.apps.airdropcryptopoints.data.networkdata.dto.UserDTO;
import silmex.apps.airdropcryptopoints.data.networkdata.response.ReferalsResponse;
import silmex.apps.airdropcryptopoints.data.networkdata.response.UserResponse;
import silmex.apps.airdropcryptopoints.data.repository.MainDataRepository;
import silmex.apps.airdropcryptopoints.network.MainService;
import silmex.apps.airdropcryptopoints.data.interfaces.CallbackI;
import silmex.apps.airdropcryptopoints.utils.ConvertUtils;
import silmex.apps.airdropcryptopoints.utils.MethodUtils;

@HiltViewModel
public class RefferralViewModel extends ViewModel {
    public MainDataRepository mainDataRepository;

    @SuppressLint("StaticFieldLeak")
    Context context;

    Retrofit retrofit;

    AppDatabase db;

    //main vars
    public MutableLiveData<Float> balance = new MutableLiveData<>(0f);
    public MutableLiveData<Integer> refCode = new MutableLiveData<>(0);
    public MutableLiveData<Boolean> isMining = new MutableLiveData<>(false);

    //presentation vars
    public MutableLiveData<String> textValue = new MutableLiveData<>("");
    public MutableLiveData<String> refferralTextValue1 = new MutableLiveData<>("");
    public MutableLiveData<String> refferralTextValue2 = new MutableLiveData<>("");
    public Integer limitOfCode = 10;
    public MutableLiveData<Float> progress = new MutableLiveData<Float>(0f);

    @Inject
    RefferralViewModel(@ApplicationContext Context context, MainDataRepository mainDataRepository, Retrofit retrofit, AppDatabase db){
        this.mainDataRepository = mainDataRepository;
        this.context = context;
        this.retrofit = retrofit;
        this.db = db;

        setUpObservers();
    }

    //main functions
    private void setUpObservers(){
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
        mainDataRepository.referralCode.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer newValue) {
                String target = refferralTextValue2.getValue();
                if(target!=null){
                    target = target.replace("#CODE#", Objects.requireNonNull(newValue).toString());

                    refferralTextValue2.postValue(target);
                }
                refCode.postValue(newValue);
                Log.d("REFFERAL1","WORKED" + refferralTextValue2.getValue());
                Log.d("REFFERAL1","WORKED" + newValue);
            }
        });

        mainDataRepository.refferalText1.observeForever(newValue -> refferralTextValue1.postValue(newValue));

        mainDataRepository.refferealText2.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String newValue) {
                if(refCode.getValue()!=null&&refCode.getValue()!=0){
                    String target = newValue.replace("#CODE#",refCode.getValue().toString());
                    target = target.replace("#LINK#","https://play.google.com/store/apps/details?id="+MainActivity.Companion.getSource());

                    refferralTextValue2.postValue(target);
                }
                else if(mainDataRepository.referralCode.getValue()!=null&&mainDataRepository.referralCode.getValue()!=0){
                    String target = newValue.replace("#CODE#",mainDataRepository.referralCode.getValue().toString());
                    target = target.replace("#LINK#","https://play.google.com/store/apps/details?id="+MainActivity.Companion.getSource());

                    refferralTextValue2.postValue(target);
                }
                Log.d("REFFERAL2","WORKED" + refferralTextValue2.getValue());
                Log.d("REFFERAL3","WORKED" + refCode.getValue());
                Log.d("REFFERAL4","WORKED" + mainDataRepository.referralCode.getValue());
            }
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

    private void updateReferralServer(String enteredCode, CallbackI callback){

        MainService mainService = retrofit.create(MainService.class);

        Call<ReferalsResponse> refResp = mainService.updateReferals(MainDataRepository.geteDeviceIdentifier(),enteredCode);
        refResp.enqueue(new Callback<ReferalsResponse>() {
            @Override
            public void onResponse(Call<ReferalsResponse> call, Response<ReferalsResponse> response) {
                if (response.isSuccessful()) {
                    ReferalsResponse referalsResponse = response.body();

                    assert referalsResponse != null;
                    callback.onSuccess(referalsResponse.success==1);
                    Log.d("network", ""+referalsResponse.success);
                }
                else {
                    Log.d("network", "failure" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<ReferalsResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
                Log.d("network", "failure" + t.getMessage());
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
        showToast("Referral code copied", true);

    }

    public void getBonusFromCodeOnClick(){

        if(isOnline()){

            if(textValue.getValue().length()==limitOfCode){
                if(mainDataRepository.hasNotEnteredCode()){
                    if(!Objects.equals(textValue.getValue(), Objects.requireNonNull(refCode.getValue()).toString())){
                        updateReferralServer(textValue.getValue(), new CallbackI() {
                            @Override
                            public void onSuccess(boolean success) {
                                getOtherCodeBonus(success);
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                showToast("Wrong referral code", false);
                                Log.e("network", "Error: " + errorMessage);
                            }
                        });
                    }
                    else{
                        showToast("You can't use your own code", false);
                    }
                }
                else{
                    showToast("You already used referral code", false);
                }
            }
            else{
                showToast("Wrong referral code", false);
            }
        }
        else{
            MainViewModel.throwConnectionError(CONNECTION_ERROR_ENUM.GET_REFCODE_BONUS_CLICK);
        }

    }

    //helper functions
    private void getOtherCodeBonus(boolean success){
        if (success) {
            log("User entered another user's code and got bonus");
            mainDataRepository.getRefferalYourCodeBonus(1);
            saveUserData();
            showToast("Bonus added", true);
            MethodUtils.safeSetValue(textValue,"");
        } else {
            showToast("Wrong referral code", false);
        }
    }


    //presentation functions
    private void showToast(String text,Boolean hasSucceded){
        MainActivity.Companion.setHasSucceded(hasSucceded);
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

        MainService mainService = retrofit.create(MainService.class);

        Call<UserResponse> userResp = mainService.getUser(MainDataRepository.geteDeviceIdentifier());
        userResp.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("network", "PROBLEM");
                    UserResponse userResponse = response.body();
                    UserDTO user = userResponse.users.get(0);

                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        MainDataTable mdt = new MainDataTable(mainDataRepository, ConvertUtils.stringToDate(user.serverTime).getTime());
                        db.mainDataDao().update(mdt);
                        Log.d("Balance",user.serverTime);
                    });
                }
                else {
                    Log.d("network", "failure" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("network", "failure" + t.getMessage());
            }
        });
    }
}
