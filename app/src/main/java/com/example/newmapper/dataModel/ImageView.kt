package com.example.newmapper.dataModel

import com.google.gson.annotations.SerializedName

data class ImageView (
    @SerializedName("_android:id")
    val androidID: String? = null,

    @SerializedName("_android:layout_width")
    val androidLayoutWidth: String? = null,

    @SerializedName("_android:layout_height")
    val androidLayoutHeight: String? = null,

    @SerializedName("_app:srcCompat")
    val appSrcCompat: String? = null,

    @SerializedName("_android:layout_x")
    val androidLayoutX: String? = null,

    @SerializedName("_android:layout_y")
    val androidLayoutY: String? = null,

    @SerializedName("_android:rotation")
    val androidRotation: String? = null
)