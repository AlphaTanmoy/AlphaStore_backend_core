package com.alphaStore.Core.minifiedresponse

import java.time.Instant

interface AccessRoleMinifiedResponse {
    var id: String
    var code: String
    var description: String?
    var title: String?
    var uname: String?
    var createdDate: Instant
}