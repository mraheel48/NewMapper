package com.example.newmapper.dataModel

import com.google.gson.annotations.SerializedName

data class AbsoluteLayout(
    @SerializedName("ImageView")
    val imageView: List<ImageView>? = null,

    @SerializedName("TextView")
    val textView: List<TextView>? = null,

    @SerializedName("_xmlns:android")
    val xmlnsAndroid: String? = null,

    @SerializedName("_xmlns:app")
    val xmlnsApp: String? = null,

    @SerializedName("_android:layout_width")
    val androidLayoutWidth: String? = null,

    @SerializedName("_android:layout_height")
    val androidLayoutHeight: String? = null
)