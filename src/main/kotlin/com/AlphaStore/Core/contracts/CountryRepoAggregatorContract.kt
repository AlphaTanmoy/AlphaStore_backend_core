package com.alphaStore.Core.contracts

import com.alphaStore.Core.entity.Country
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.minifiedresponseimpl.CountryListMinifiedResponseImpl
import com.alphaStore.Core.minifiedresponseimpl.FetchMostRecentMinifiedResponseImpl
import com.alphaStore.Core.model.AggregatorListResponse
import com.alphaStore.Core.model.AggregatorResponse
import java.time.ZonedDateTime

interface CountryRepoAggregatorContract {

    fun save(entity :Country) : Country

    fun saveAll(entities: List<Country>)

    fun findByKnownNameAndDataStatus(
        knownName: String,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorListResponse<Country>

    fun findByOfficialNameAndDataStatus(
        officialName: String,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorListResponse<Country>

    fun executeFunction(queryString: String): List<Country>

    fun findByIdAndDataStatus(
        id: String,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorListResponse<Country>

    fun findByIsdCodeAndDataStatus(
        isdCode: String,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorListResponse<Country>

    fun findTop1ByOrderByCreatedDateAsc(
        skipCache: Boolean = false,
    ): AggregatorListResponse<FetchMostRecentMinifiedResponseImpl>

    fun findCountWithOutOffsetIdOffsetDateAndLimit(
        queryString: String,
        serviceable: Boolean? = null,
        skipCache: Boolean = false,
    ): AggregatorResponse<Long>

    fun findAllDataWithOutOffsetIdOffsetDateAndLimit(
        queryString: String,
        serviceable: Boolean? = null,
        skipCache: Boolean = false,
    ): AggregatorListResponse<CountryListMinifiedResponseImpl>

    fun findWithOutOffsetId(
        queryString: String,
        zonedDateTime: ZonedDateTime,
        serviceable: Boolean?,
        limit: Int,
        skipCache: Boolean = false,
    ): AggregatorListResponse<CountryListMinifiedResponseImpl>

    fun findWithOffsetId(
        queryString: String,
        offsetDate: ZonedDateTime,
        offsetId: String,
        serviceable: Boolean?,
        limit: Int,
        skipCache: Boolean = false,
    ): AggregatorListResponse<CountryListMinifiedResponseImpl>

    fun executeGlobalFunction(queryString: String): List<List<Any>>

    fun findByAlpha2AndDataStatus(
        alpha2Code: String,

        skipCache: Boolean = false
    ): AggregatorListResponse<Country>

    fun findCountWithOutOffsetIdAndDate(
        queryString: String,
        serviceable: Boolean?,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean  = false,
    ): AggregatorResponse<Long>

    fun findDataWithOutOffsetIdAndDate(
        queryString: String,
        serviceable: Boolean?,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean  = false,
    ): AggregatorListResponse<CountryListMinifiedResponseImpl>

    fun findDataWithOutOffsetId(
        queryString: String,
        serviceable: Boolean?,
        skipCache: Boolean  = false,
        limit: Int,
        offsetDate: ZonedDateTime,
        dataStatus: DataStatus = DataStatus.ACTIVE
    ): AggregatorListResponse<CountryListMinifiedResponseImpl>

    fun findDataWithOffsetId(
        queryString: String,
        serviceable: Boolean?,
        offsetId: String,
        limit: Int,
        offsetDate: ZonedDateTime,
        skipCache: Boolean  = false
    ): AggregatorListResponse<CountryListMinifiedResponseImpl>
}