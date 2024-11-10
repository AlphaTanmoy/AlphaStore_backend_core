package com.alphaStore.Core.minifiedresponseimpl

import com.alphaStore.Core.minifiedresponse.FetchMostRecentMinifiedResponse
import java.time.Instant

data class FetchMostRecentMinifiedResponseImpl(
    override var id: String,
    override var createdDate: Instant
) : FetchMostRecentMinifiedResponse