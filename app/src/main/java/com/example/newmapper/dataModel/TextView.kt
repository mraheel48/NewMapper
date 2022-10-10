package com.example.newmapper.dataModel

import com.fasterxml.jackson.annotation.JsonProperty

data class TextView (
    @JsonProperty("_android:id")
    val androidID: String? = null,

    @JsonProperty("_android:layout_width")
    val androidLayoutWidth: String? = null,

    @JsonProperty("_android:layout_height")
    val androidLayoutHeight: String? = null,

    @JsonProperty("_android:layout_x")
    val androidLayoutX: String? = null,

    @JsonProperty("_android:layout_y")
    val androidLayoutY: String? = null,

    @JsonProperty("_android:fontFamily")
    val androidFontFamily: String? = null,

    @JsonProperty("_android:rotation")
    val androidRotation: String? = null,

    @JsonProperty("_android:text")
    val androidText: String? = null,

    @JsonProperty("_android:textColor")
    val androidTextColor: String? = null,

    @JsonProperty("_android:textSize")
    val androidTextSize: String? = null,

    @JsonProperty("_android:padding")
    val androidPadding: String? = null
)

