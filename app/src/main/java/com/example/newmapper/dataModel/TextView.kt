package com.example.newmapper.dataModel

import com.google.gson.annotations.SerializedName

data class TextView (
    @SerializedName("_android:id")
    val androidID: String? = null,

    @SerializedName("_android:layout_width")
    val androidLayoutWidth: String? = null,

    @SerializedName("_android:layout_height")
    val androidLayoutHeight: String? = null,

    @SerializedName("_android:layout_x")
    val androidLayoutX: String? = null,

    @SerializedName("_android:layout_y")
    val androidLayoutY: String? = null,

    @SerializedName("_android:fontFamily")
    val androidFontFamily: String? = null,

    @SerializedName("_android:rotation")
    val androidRotation: String? = null,

    @SerializedName("_android:text")
    val androidText: String? = null,

    @SerializedName("_android:textColor")
    val androidTextColor: String? = null,

    @SerializedName("_android:textSize")
    val androidTextSize: String? = null,

    @SerializedName("_android:padding")
    val androidPadding: String? = null
)

