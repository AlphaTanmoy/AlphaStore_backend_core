package com.alphaStore.Core.entity

import com.alphaStore.Core.enums.ClientDevicePlatform
import com.fasterxml.jackson.annotation.JsonFilter
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity(name = "client_devices")
data class ClientDevice(
    @Enumerated(EnumType.STRING)
    var platform: ClientDevicePlatform = ClientDevicePlatform.ANDROID,
    var userAgent: String = "",
    var name: String = "",
    var uniqueIdentifierId: String = "",
    var lastUsed: ZonedDateTime = ZonedDateTime.now(),
    var trusted: Boolean = false,
    var fcmId: String = "",
    var fcmIdBeingUsedElseWhere: Boolean = false,
    var active: Boolean = true,
    /*@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user:User = User(),*/
) : SuperEntityWithIdCreatedLastModifiedDataStatus()

@JsonFilter("ClientDeviceFilter")
class ClientDeviceMixIn