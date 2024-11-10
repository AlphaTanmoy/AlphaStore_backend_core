package com.alphaStore.Core.model

import java.util.*

data class AggregatorListResponse<T>(
    var data: ArrayList<T> = arrayListOf(),
    var databaseAccessLogId: String = UUID.randomUUID().toString()
)