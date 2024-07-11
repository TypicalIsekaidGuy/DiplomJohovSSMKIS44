package silmex.apps.airdropcryptopoints.data.db.maindata;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import silmex.apps.airdropcryptopoints.data.model.MULTIPLYER_ENUM;
import silmex.apps.airdropcryptopoints.data.repository.MainDataRepository;
import silmex.apps.airdropcryptopoints.utils.IntegerUtils;

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
    @ColumnInfo(name = "current_chosen_multipliyer_value")
    public int currentChosenMultipliyerValue;
    @NotNull
    @ColumnInfo(name = "referals")
    public int referals;

    @NotNull
    @ColumnInfo(name = "is_active")
    public boolean isActive;

    @ColumnInfo(name = "exit_time")
    public long exit_time;

    @ColumnInfo(name = "estimated_end_time")
    public long estimated_end_time;


    public MainDataTable(MainDataRepository mdr, Long exit_time){
        if(MainDataRepository.random_for_save==null){
            this.random_save_id = IntegerUtils.generateRandomInteger();
        }
        else{
            this.random_save_id = MainDataRepository.random_for_save;
        }
        didShowLearning = Boolean.TRUE.equals(mdr.didShowLearning.getValue());
        try {
            balance = mdr.balance.getValue();
        }
        catch (Exception _){
            Log.d("Error","Here");
            balance = 0;
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
            this.estimated_end_time = mdr.millisUntilFinishedLiveData.getValue();
        }
        catch (Exception _){
            Log.d("Error","Here");
            this.estimated_end_time = 0;
        }
        this.referals = mdr.referals;
        this.exit_time = exit_time;
    }
    public MainDataTable(){
        this.random_save_id = IntegerUtils.generateRandomInteger();
        didShowLearning = false;
        balance = 0F;
        currentChosenMultipliyerValue = 1;
        isActive = false;
        this.exit_time = 0;
        this.estimated_end_time = 0;
        this.referals = 0;
    }

    public MainDataTable(int random_save_id, long exit_time){
        this.random_save_id = random_save_id;
        didShowLearning = false;
        balance = 0F;
        currentChosenMultipliyerValue = 1;
        isActive = false;
        this.exit_time = exit_time;
        this.estimated_end_time = 0;
        this.referals = 0;
    }
    public MainDataTable(int random_save_id,boolean didShowLearning, float balance, int currentChosenMultipliyerValue, boolean isActive, long exit_time, long estimated_end_time, int referals){
        this.random_save_id = random_save_id;
        this.didShowLearning = didShowLearning;
        this.balance = balance;
        this.currentChosenMultipliyerValue = currentChosenMultipliyerValue;
        this.isActive = isActive;
        this.exit_time = exit_time;
        this.estimated_end_time = estimated_end_time;
        this.referals = referals;
    }
}
