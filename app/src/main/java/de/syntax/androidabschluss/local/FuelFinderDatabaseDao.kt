package de.syntax.androidabschluss.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.syntax.androidabschluss.data.model.Station

@Dao
interface FuelFinderDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(station: Station)

    // Methode zum Löschen einer Notiz aus der Datenbank
    @Delete
    suspend fun deleteStation(station: Station)

    // Methode zum Abrufen aller Notizen aus der Datenbank
    @Query("SELECT * FROM Station")
    fun getAll(): LiveData<List<Station>>
}