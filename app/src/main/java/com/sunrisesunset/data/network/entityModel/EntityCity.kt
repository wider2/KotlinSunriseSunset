package com.sunrisesunset.data.network.entityModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class EntityCity(
    var lat: Double,
    var lon: Double,
    var city: String,
    var term: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}