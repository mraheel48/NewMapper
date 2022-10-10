package com.example.newmapper.dataModel

import com.fasterxml.jackson.annotation.JsonProperty

data class MapperModel (
    @JsonProperty("AbsoluteLayout")
    val absoluteLayout: AbsoluteLayout? = null
)