package com.ivos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ivos.data.local.model.CityModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCitiesDao {

    @Query("SELECT * FROM favorite_cities")
    fun datFavoriteCities(): Flow<List<CityModel>>

    @Query("SELECT EXISTS (SELECT * FROM favorite_cities WHERE id=:cityId LIMIT 1)")
    fun observeCityIsFavorite(cityId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCityToFavorite(cityModel: CityModel)

    @Query("DELETE FROM favorite_cities WHERE id=:cityId")
    fun removeCityFromFavorite(cityId: Int)
}
