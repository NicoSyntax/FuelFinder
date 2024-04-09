package de.syntax.androidabschluss.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.syntax.androidabschluss.data.model.Station

@Database(entities = [Station::class], version = 1)
abstract class StationDatabase : RoomDatabase() {
    abstract val FuelFinderDatabaseDao: FuelFinderDatabaseDao
}

private lateinit var INSTANCE: StationDatabase

fun getDatabase(context: Context): StationDatabase {
    synchronized(StationDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                StationDatabase::class.java,
                "StationDatabase"
            ).build()
        }
    }
    return INSTANCE
}