package com.ivos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ivos.data.local.dao.FavoriteCitiesDao
import com.ivos.data.local.model.CityModel

@Database(
    entities = [CityModel::class],
    version = 1,
    exportSchema = false
)
abstract class IvosWeatherDB: RoomDatabase() {

    abstract fun favoriteCitiesDao(): FavoriteCitiesDao

    companion object {

        private const val DB_NAME = "IvosWeatherDB"
        private var INSTANCE: IvosWeatherDB? = null
        private val LOCK = Any()

        fun getInstance(context: Context): IvosWeatherDB {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }

                val db = Room.databaseBuilder(
                    context = context,
                    klass = IvosWeatherDB::class.java,
                    name = DB_NAME,
                ).build()

                INSTANCE = db
                return db
            }
        }
    }
}
