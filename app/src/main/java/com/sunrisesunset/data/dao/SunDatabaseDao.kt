package com.sunrisesunset.data.dao

import androidx.room.*
import com.sunrisesunset.data.network.entityModel.EntityCity

// SQL operations for Google Room
@Dao
interface SunDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(cities: EntityCity) : Long

    @Query("SELECT * from city_table WHERE id= :id")
    fun getItemById(id: Int) : List<EntityCity>

    @Query("SELECT * from city_table")
    fun getAll(): List<EntityCity>

    @Query("DELETE FROM city_table")
    fun clearCities()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCities(city: List<EntityCity>)

    @Query("SELECT * FROM city_table WHERE term= :term")
    fun getByQuery(term: String) : EntityCity?

    @Query("SELECT id FROM city_table WHERE term = :term LIMIT 1")
    fun getItemId(term: String): Int?
}