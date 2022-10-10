package com.example.newmapper

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class JsonDataParser(
    @SerializedName("json_id")
    val id: String,
    @SerializedName("json_name")
    val name: String,
    @SerializedName("json_image")
    val image: String,
    @SerializedName("json_description")
    val description: String
)