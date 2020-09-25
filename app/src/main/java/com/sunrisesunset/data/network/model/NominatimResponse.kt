package com.sunrisesunset.model

import com.google.gson.annotations.SerializedName

data class NominatimResponse(
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lon: String,
    @SerializedName("display_name") val display_name: String
)