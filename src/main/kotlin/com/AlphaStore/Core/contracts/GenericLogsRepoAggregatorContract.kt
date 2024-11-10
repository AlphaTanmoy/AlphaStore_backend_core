package com.alphaStore.Core.contracts

import com.alphaStore.Core.entity.GenericLogs
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.minifiedresponseimpl.FetchMostRecentMinifiedResponseImpl
import com.alphaStore.Core.model.AggregatorListResponse
import com.alphaStore.Core.model.AggregatorResponse
import java.time.ZonedDateTime

interface GenericLogsRepoAggregatorContract {

    fun save(entity: GenericLogs): GenericLogs

    fun saveAll(entities: List<GenericLogs>)

    fun executeFunction(queryToExecute: String): List<GenericLogs>

    fun dropTable()

    fun findTop1ByDataStatusOrderByCreatedDateAsc(
        skipCache: Boolean = false,
    ): AggregatorListResponse<GenericLogs>

    fun findByClientDeviceIdAndDataStatus(
        clientDeviceId: String,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorListResponse<GenericLogs>

    fun findByDataOrderByCreatedDateDesc(
        sessionTrackingId: String? = null,
        genericLogsIdParam: String? = null,
        userId: String? = null,
        userType: String? = null,
        apiTire: String? = null,
        clientDeviceId: String? = null,
        httpMethod: String? = null,
        api: String? = null,
        fromDate: ZonedDateTime? = null,
        toDate: ZonedDateTime? = null,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorListResponse<GenericLogs>

    fun countByData(
        sessionTrackingId: String? = null,
        genericLogsIdParam: String? = null,
        userId: String? = null,
        userType: String? = null,
        apiTire: String? = null,
        clientDeviceId: String? = null,
        api: String? = null,
        httpMethod: String? = null,
        fromDate: ZonedDateTime? = null,
        toDate: ZonedDateTime? = null,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorResponse<Long>


    fun findByDataWithOffsetId(
        sessionTrackingId: String? = null,
        genericLogsIdParam: String? = null,
        userId: String? = null,
        userType: String? = null,
        apiTire: String? = null,
        clientDeviceId: String? = null,
        api: String? = null,
        httpMethod: String? = null,
        fromDate: ZonedDateTime? = null,
        toDate: ZonedDateTime? = null,
        offsetId: String = "",
        offsetDate: ZonedDateTime,
        limit: Int,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorListResponse<GenericLogs>


    fun findByDataWithOutOffsetId(
        sessionTrackingId: String? = null,
        genericLogsIdParam: String? = null,
        userId: String? = null,
        userType: String? = null,
        apiTire: String? = null,
        clientDeviceId: String? = null,
        api: String? = null,
        httpMethod: String? = null,
        fromDate: ZonedDateTime? = null,
        toDate: ZonedDateTime? = null,
        offsetDate: ZonedDateTime,
        limit: Int,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorListResponse<GenericLogs>


    fun findUniqueSessions(
        userId: String? = null,
        skipCache: Boolean = false,
    ): AggregatorListResponse<GenericLogs>

    fun findAllApiLogGroupByApis(
        skipCache: Boolean = false,
    ): AggregatorListResponse<GenericLogs>

    fun findByUserIdAndSuccessFulAndDataStatus(
        userId: String,
        successFul: Boolean,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = true
    ): AggregatorListResponse<GenericLogs>

    fun disableAllFailsForUserId(
        successFul: Boolean,
        userId: String,
        dataStatus: DataStatus = DataStatus.INACTIVE
    )

    fun findTop1ByOrderByCreatedDateDesc(): AggregatorListResponse<FetchMostRecentMinifiedResponseImpl>

    fun findCountWithoutOffsetIdAndDate(
        userId: String?,
        treeCode: String,
        api: String? = null,
        applicationGroupCode: String?,
        applicationCode: String?,
        fromDateFinal: ZonedDateTime? = null,
        toDateFinal: ZonedDateTime? = null
    ): AggregatorResponse<Long>

    fun findDataWithOutOffsetIdAndDate(
        userId: String?,
        treeCode: String,
        api: String? = null,
        applicationGroupCode: String?,
        applicationCode: String?,
        fromDateFinal: ZonedDateTime? = null,
        toDateFinal: ZonedDateTime? = null
    ): AggregatorListResponse<GenericLogs>

    fun findDataWithOutOffsetId(
        userId: String?,
        treeCode: String,
        api: String? = null,
        applicationGroupCode: String?,
        applicationCode: String?,
        fromDateFinal: ZonedDateTime? = null,
        toDateFinal: ZonedDateTime? = null,
        offsetDate: ZonedDateTime,
        limit: Int
    ): AggregatorListResponse<GenericLogs>

    fun findDataWithOffsetId(
        userId: String?,
        treeCode: String,
        api: String? = null,
        applicationGroupCode: String?,
        applicationCode: String?,
        fromDateFinal: ZonedDateTime? = null,
        toDateFinal: ZonedDateTime? = null,
        offsetId: String,
        offsetDate: ZonedDateTime,
        limit: Int
    ): AggregatorListResponse<GenericLogs>

}