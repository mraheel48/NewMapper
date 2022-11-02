package com.example.newmapper.dataModel

import com.fasterxml.jackson.annotation.JsonProperty

data class TextView(
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

    @JsonProperty("_android:textAlignment")
    val androidTextAlignment: String? = null,

    @JsonProperty("_android:textColor")
    val androidTextColor: String? = null,

    @JsonProperty("_android:textSize")
    val androidTextSize: String? = null,

    @JsonProperty("_android:shadowColor")
    val androidShadowColor: String? = null,

    @JsonProperty("_android:shadowDx")
    val androidShadowDx: String? = null,

    @JsonProperty("_android:shadowDy")
    val androidShadowDy: String? = null,

    @JsonProperty("_android:shadowRadius")
    val androidShadowRadius: String? = null,

    @JsonProperty("_android:alpha")
    val androidAlpha: String? = null,

    @JsonProperty("_app:autoSizeTextType")
    val appAutoSizeTextType: String? = null,

    @JsonProperty("_android:tag")
    val androidTag: String? = null,

    @JsonProperty("_android:letterSpacing")
    val androidLetterSpacing: String? = null,
)




