package com.sunrisesunset.model

import com.google.gson.annotations.SerializedName

data class SunResponse(
    @SerializedName("results") val results: Results) {
    data class Results(
        @SerializedName("sunrise") val sunrise: String,
        @SerializedName("sunset") val sunset: String,
        @SerializedName("day_length") val day_length: Int,
        @SerializedName("civil_twilight_begin") val civil_twilight_begin: String,
        @SerializedName("civil_twilight_end") val civil_twilight_end: String
    )
}