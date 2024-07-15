package silmex.apps.airdropcryptopoints.viewmodel;


import static silmex.apps.airdropcryptopoints.data.repository.MainDataRepository.fullTimerDuration;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import silmex.apps.airdropcryptopoints.MainActivity;
import silmex.apps.airdropcryptopoints.data.db.AppDatabase;
import silmex.apps.airdropcryptopoints.data.db.maindata.MainDataTable;
import silmex.apps.airdropcryptopoints.data.model.MULTIPLYER_ENUM;
import silmex.apps.airdropcryptopoints.data.model.Transaction;
import silmex.apps.airdropcryptopoints.data.networkdata.dto.TransactionDTO;
import silmex.apps.airdropcryptopoints.data.networkdata.dto.UserDTO;
import silmex.apps.airdropcryptopoints.data.networkdata.response.CreateTransactionResponse;
import silmex.apps.airdropcryptopoints.data.networkdata.response.TransactionResponse;
import silmex.apps.airdropcryptopoints.data.networkdata.response.UserResponse;
import silmex.apps.airdropcryptopoints.data.repository.MainDataRepository;
import silmex.apps.airdropcryptopoints.network.MainService;
import silmex.apps.airdropcryptopoints.network.RetrofitClient;
import silmex.apps.airdropcryptopoints.network.WithdrawalService;
import silmex.apps.airdropcryptopoints.ui.view.composables.Coin;
import silmex.apps.airdropcryptopoints.utils.ConvertUtils;
import silmex.apps.airdropcryptopoints.utils.StringUtils;

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
    public MutableLiveData<List<Transaction>> transactionList = new MutableLiveData<>(new ArrayList<>());

    //presentation vars
    public MutableLiveData<Float> progress = new MutableLiveData<Float>(0f);

    @Inject
    WithdrawalViewModel(@ApplicationContext Context context,MainDataRepository mainDataRepository,AppDatabase db, Retrofit retrofit){
        this.mainDataRepository = mainDataRepository;
        this.context = context;
        this.retrofit = retrofit;
        this.db = db;

        getTransaction();
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
                if(newValue!=0){

                    updateProgress(newValue);

                    if(newValue<=500L){
                        onTimerEnd();
                    }
                }
            }
        });
    }


    //presentation functions
    public void onTimerEnd(){

    }
    public void showToast(String text,Boolean hasSucceded){
        MainActivity.Companion.setHasSucceded(hasSucceded);
        MainActivity.Companion.getToastText().setValue(text);
    }
    public void updateProgress(Long estimatedTime){
        progress.setValue( ((float)estimatedTime/fullTimerDuration));
    }



    //onClick functions
    public void withdrawalOnClick(){
        if(!isMining.getValue()){
                WithdrawalService serviceTrans = RetrofitClient.getClient().create(WithdrawalService.class);
                Float bucks = mainDataRepository.getBalanceForWithdrawal();
                if(bucks!=null){

                    Call<CreateTransactionResponse> created = serviceTrans.createTransaction(StringUtils.generateDeviceIdentifier(), bucks,"Generated from mobile app = " +MainActivity.Companion.getSource(),MainActivity.Companion.getSource());
                    created.enqueue(new Callback<CreateTransactionResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<CreateTransactionResponse> call, @NonNull Response<CreateTransactionResponse> response) {
                            if (response.isSuccessful()) {
                                CreateTransactionResponse transResponse = response.body();
                                if (transResponse != null) {
                                    if(transResponse.success==1){

                                        MainViewModel.log("User made withdrawal for: "+bucks);
                                        mainDataRepository.resetBalance();
                                        saveUserData();
                                        getTransaction();
                                        Log.d("VIEWMODELTESTS",""+mainDataRepository.transactionList.getValue().size());
                                        Log.d("VIEWMODELTESTS",""+transactionList.getValue().size());

                                    }
                                }
                                else{

                                }
                            } else {
                                Log.d("network", "failure" + response.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<CreateTransactionResponse> call, Throwable t) {
                            Log.d("network", "failure" + t.getMessage());
                        }
                    });
                }
        }
        else{
            showToast("Don't forget to claim your points when the timer ends",false);
        }

    }

    public void copyCodeOnClick(String text){

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        showToast("Code copied", true);

    }

    //helper functions

    private void getTransaction() {
        Call<TransactionResponse> transResp = retrofit.create(WithdrawalService.class).getTransactions(StringUtils.generateDeviceIdentifier());

        transResp.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(@NonNull Call<TransactionResponse> call, @NonNull Response<TransactionResponse> response) {
                if (response.isSuccessful()) {
                    TransactionResponse transResponse = response.body();

                    if (transResponse.moneyList != null) {

                        List<Transaction> replaceList = new ArrayList<>();
                        for (TransactionDTO trans : transResponse.moneyList) {
                            if(Objects.equals(trans.source, MainActivity.Companion.getSource())){
                                replaceList.add(new Transaction(trans));
                            }
                        }

                        Collections.sort(replaceList);
                        Collections.reverse(replaceList);
                        mainDataRepository.transactionList.setValue(replaceList);
                        transactionList.setValue(replaceList);

                    }
                    else{
                        Log.d("network","Problem");
                    }
                } else {
                    Log.d("network", "failure" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                Log.d("network", "failure" + t.getMessage());
            }
        });
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
