package com.johov.bitcoin.data.db.maindata;

import static com.johov.bitcoin.data.repository.MainDataRepository.didShowRateWindow;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import com.johov.bitcoin.data.repository.MainDataRepository;
import com.johov.bitcoin.utils.IntegerUtils;

@Entity(tableName = "main_data_table")
public class MainDataTable {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "random_save_id")
    public int random_save_id;

    @NotNull
    @ColumnInfo(name = "did_show_learning")
    public boolean didShowLearning;

    @NotNull
    @ColumnInfo(name = "balance")
    public float balance;

    @NotNull
    @ColumnInfo(name = "claimed_balance")
    public float claimed_balance;

    @NotNull
    @ColumnInfo(name = "current_chosen_multipliyer_value")
    public int currentChosenMultipliyerValue;
    @NotNull
    @ColumnInfo(name = "referals")
    public int referals;

    @NotNull
    @ColumnInfo(name = "is_active")
    public boolean isActive;

    @NotNull
    @ColumnInfo(name = "can_withdraw")
    public boolean can_withdraw;

    @ColumnInfo(name = "exit_time")
    public long exit_time;

    @ColumnInfo(name = "estimated_end_time")
    public long estimated_end_time;

    @ColumnInfo(name = "cooldown_estimated_end_time")
    public long cooldown_estimated_end_time;

    @ColumnInfo(name = "did_show_rate_window")//new
    public boolean did_show_rate_window;//new

    public MainDataTable(){
        this.random_save_id = IntegerUtils.generateRandomInteger();
        didShowLearning = false;
        balance = 0F;
        claimed_balance = 0F;
        currentChosenMultipliyerValue = 1;
        isActive = false;
        can_withdraw = false;
        this.exit_time = 0;
        this.cooldown_estimated_end_time = 0;
        this.estimated_end_time = 0;
        this.referals = 0;
        this.did_show_rate_window =false;//new
    }
    public MainDataTable(MainDataRepository mdr, Long exit_time){
        if(MainDataRepository.random_for_save.getValue()==null){
            this.random_save_id = IntegerUtils.generateRandomInteger();
        }
        else{
            this.random_save_id = MainDataRepository.random_for_save.getValue();
        }
        didShowLearning = Boolean.TRUE.equals(mdr.didShowLearning.getValue());
        try {
            balance = mdr.balance.getValue();
        }
        catch (Exception e){
            Log.d("Error","Here");
            balance = 0;
        }
        try {
            claimed_balance = mdr.claimedBalance.getValue();
        }
        catch (Exception e){
            Log.d("Error","Here");
            claimed_balance = 0;
        }
        try {
            currentChosenMultipliyerValue = mdr.currentChosenMultipliyer.getValue().getValue();
        }
        catch (Exception _){
            Log.d("Error","Here");
            currentChosenMultipliyerValue = 1;
        }
        try {
            isActive = Boolean.TRUE.equals(mdr.isActive.getValue());
        }
        catch (Exception _){
            Log.d("Error","Here");
            isActive = false;
        }
        try {
            can_withdraw = Boolean.TRUE.equals(mdr.canWithdraw.getValue());
        }
        catch (Exception _){
            Log.d("Error","Here");
            can_withdraw = false;
        }
        try {
            this.estimated_end_time = mdr.millisUntilFinishedLiveData.getValue();
        }
        catch (Exception _){
            Log.d("Error","Here");
            this.estimated_end_time = 0;
        }
        if(mdr.cooldownmillisUntilFinishedLiveData.getValue()!=null){
            this.cooldown_estimated_end_time = mdr.cooldownmillisUntilFinishedLiveData.getValue();
        }
        else{
            this.cooldown_estimated_end_time = 0;
        }
        if(mdr.referals.getValue()!=null){
            Log.d("DBTEST","no error "+mdr.referals.getValue());
            this.referals = mdr.referals.getValue();
        }
        else{
            Log.d("DBTEST","ERROR");
            this.referals = 0;
        }
        if(didShowRateWindow.getValue()!=null){//new
            Log.d("DBTEST","no error "+mdr.referals.getValue());
            this.did_show_rate_window = Boolean.TRUE.equals(didShowRateWindow.getValue());
        }
        else{
            Log.d("DBTEST","ERROR");
            this.did_show_rate_window =false;
        }
        this.exit_time = exit_time;
    }


    public MainDataTable(int random_save_id, long exit_time){
        this.random_save_id = random_save_id;
        didShowLearning = false;
        balance = 0F;
        claimed_balance = 0F;
        currentChosenMultipliyerValue = 1;
        isActive = false;
        can_withdraw = false;
        this.exit_time = exit_time;
        this.estimated_end_time = 0;
        this.cooldown_estimated_end_time = 0;
        this.referals = 0;
        this.did_show_rate_window =false;//new
    }
    public MainDataTable(int random_save_id,boolean didShowLearning, float balance,float claimed_balance, int currentChosenMultipliyerValue, boolean isActive,boolean can_withdraw, long exit_time, long estimated_end_time,long cooldown_estimated_end_time, int referals, boolean did_show_rate_window){
        this.random_save_id = random_save_id;
        this.didShowLearning = didShowLearning;
        this.balance = balance;
        this.claimed_balance = claimed_balance;
        this.currentChosenMultipliyerValue = currentChosenMultipliyerValue;
        this.isActive = isActive;
        this.can_withdraw = can_withdraw;
        this.exit_time = exit_time;
        this.estimated_end_time = estimated_end_time;
        this.cooldown_estimated_end_time = cooldown_estimated_end_time;
        this.referals = referals;
        this.did_show_rate_window =did_show_rate_window;//new
    }
}
