package com.example.newmapper.dataModel

import com.fasterxml.jackson.annotation.JsonProperty

data class ImageView(
    @JsonProperty("_android:id")
    val androidID: String? = null,

    @JsonProperty("_android:layout_width")
    val androidLayoutWidth: String? = null,

    @JsonProperty("_android:layout_height")
    val androidLayoutHeight: String? = null,

    @JsonProperty("_app:srcCompat")
    val appSrcCompat: String? = null,

    @JsonProperty("_android:layout_x")
    val androidLayoutX: String? = null,

    @JsonProperty("_android:layout_y")
    val androidLayoutY: String? = null,

    @JsonProperty("_android:rotation")
    val androidRotation: String? = null,

    @JsonProperty("_android:tag")
    val androidTag: String? = null
)