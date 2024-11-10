package com.alphaStore.Core.entity

import com.alphaStore.Core.enums.ApiTire
import com.alphaStore.Core.enums.HttpMethod
import com.fasterxml.jackson.annotation.JsonFilter
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity(name = "generic_logs")
data class GenericLogs(
    var api: String = "",
    @Column(nullable = true, length = 10000)
    var payload: String? = null,
    @Enumerated(EnumType.STRING)
    var httpMethod: HttpMethod = HttpMethod.POST,
    @Column(nullable = true)
    var userId: String? = null,
    @Enumerated(EnumType.STRING)
    var tokenTire: ApiTire? = null,
    var apiKeyUsed: String? = null,
    @Column(nullable = true)
    var clientDeviceId: String? = null,
    var successful : Boolean = true,
) : SuperEntityWithIdCreatedLastModifiedDataStatus()

@JsonFilter("GenericLogsFilter")
class GenericLogsMixIn