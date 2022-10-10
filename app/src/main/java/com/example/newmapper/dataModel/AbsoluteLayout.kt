package com.example.newmapper.dataModel

import com.fasterxml.jackson.annotation.JsonProperty

data class AbsoluteLayout(
    @JsonProperty("ImageView")
    val imageView: List<ImageView>? = null,

    @JsonProperty("TextView")
    val textView: List<TextView>? = null,

    @JsonProperty("_xmlns:android")
    val xmlnsAndroid: String? = null,

    @JsonProperty("_xmlns:app")
    val xmlnsApp: String? = null,

    @JsonProperty("_android:layout_width")
    val androidLayoutWidth: String? = null,

    @JsonProperty("_android:layout_height")
    val androidLayoutHeight: String? = null
)