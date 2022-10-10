package com.example.newmapper.dataModel

import com.google.gson.annotations.SerializedName

data class MapperModel (
    @SerializedName("AbsoluteLayout")
    val absoluteLayout: AbsoluteLayout? = null
)