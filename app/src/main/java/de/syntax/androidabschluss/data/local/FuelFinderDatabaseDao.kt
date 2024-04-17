package de.syntax.androidabschluss.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.syntax.androidabschluss.data.model.Station

@Dao
interface FuelFinderDatabaseDao {

    //Insert Station into database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(station: Station)

    //delete Station from database
    @Delete
    suspend fun deleteStation(station: Station)

    @Query("SELECT * FROM Station")
    fun getAll(): LiveData<List<Station>>
}