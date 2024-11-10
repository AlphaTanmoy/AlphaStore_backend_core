package com.alphaStore.Core.model

import java.util.*

data class AggregatorResponse<T>(
    var data: T,
    var databaseAccessLogId: String = UUID.randomUUID().toString()
)