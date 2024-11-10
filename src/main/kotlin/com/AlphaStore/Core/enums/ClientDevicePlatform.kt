package com.alphaStore.Core.enums

import com.fasterxml.jackson.annotation.JsonProperty

enum class ClientDevicePlatform(val nameDescriptor: String) {
    @JsonProperty("WINDOWS_DESKTOP")
    WINDOWS_DESKTOP("Windows Desktop"),

    @JsonProperty("MAC")
    MAC("Mac"),

    @JsonProperty("ANDROID")
    ANDROID("Android"),

    @JsonProperty("IOS")
    IOS("iOS"),
}