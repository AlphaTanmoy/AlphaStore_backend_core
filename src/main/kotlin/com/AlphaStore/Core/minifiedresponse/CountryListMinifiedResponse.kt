package com.alphaStore.Core.minifiedresponse

import java.time.Instant

interface CountryListMinifiedResponse {
    var id: String
    var name: String
    var officialName: String
    var isdCode: String
    var alpha2: String
    var alpha3: String
    var createdDate: Instant
}