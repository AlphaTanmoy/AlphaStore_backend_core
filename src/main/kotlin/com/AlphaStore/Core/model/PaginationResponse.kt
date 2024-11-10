package com.alphaStore.Core.model

import com.fasterxml.jackson.annotation.JsonFilter
import java.io.Serializable

data class PaginationResponse<T>(
    var data: ArrayList<T> = arrayListOf(),
    var offsetToken: String? = null,
    var recordCount: Int = 0,
    var filterUsed: ArrayList<FilterOption> = ArrayList(),
    var miscIdOne: String = "",
    var miscIdTwo: String = "",
) : Serializable

@JsonFilter("paginationResponseFilter")
class PaginationResponseMixIn