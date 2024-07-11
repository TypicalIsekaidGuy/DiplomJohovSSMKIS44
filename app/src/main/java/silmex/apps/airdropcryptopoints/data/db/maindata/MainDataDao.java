package silmex.apps.airdropcryptopoints.data.db.maindata;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MainDataDao {

    @Insert
    abstract void insert(MainDataTable data);
    @Update
    abstract void update(MainDataTable data);


    @Query("SELECT * FROM main_data_table")
    abstract MainDataTable get();
}
