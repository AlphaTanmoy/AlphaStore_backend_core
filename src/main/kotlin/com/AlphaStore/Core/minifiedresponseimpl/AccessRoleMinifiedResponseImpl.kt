package com.alphaStore.Core.minifiedresponseimpl

import com.alphaStore.Core.minifiedresponse.AccessRoleMinifiedResponse
import java.time.Instant

data class AccessRoleMinifiedResponseImpl(
    override var id: String,
    override var code: String,
    override var description: String? = null,
    override var title: String? = null,
    override var uname: String? = null,
    override var createdDate: Instant
) : AccessRoleMinifiedResponse
