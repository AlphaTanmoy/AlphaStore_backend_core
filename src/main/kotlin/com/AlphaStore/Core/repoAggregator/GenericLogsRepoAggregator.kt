package com.alphaStore.Core.repoAggregator

import com.alphaStore.Core.contracts.GenericLogsRepoAggregatorContract
import com.alphaStore.Core.entity.GenericLogs
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.minifiedresponseimpl.FetchMostRecentMinifiedResponseImpl
import com.alphaStore.Core.model.AggregatorListResponse
import com.alphaStore.Core.model.AggregatorResponse
import com.alphaStore.Core.repo.GenericLogsRepo
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.ArrayList

@Component
class GenericLogsRepoAggregator(
    private val genericLogsRepo: GenericLogsRepo,
) : GenericLogsRepoAggregatorContract {

    override fun save(entity: GenericLogs): GenericLogs {
        return genericLogsRepo.save(entity)
    }

    override fun saveAll(entities: List<GenericLogs>) {
        genericLogsRepo.saveAll(entities)
    }

    override fun executeFunction(queryToExecute: String): List<GenericLogs> {
        return genericLogsRepo.executeFunction(queryToExecute)
    }

    override fun dropTable() {
        genericLogsRepo.dropTable()
    }

    override fun findTop1ByDataStatusOrderByCreatedDateAsc(
        skipCache: Boolean
    ): AggregatorListResponse<GenericLogs> {
        val databaseAccessLogId = UUID.randomUUID().toString()

        val resultFromDb = genericLogsRepo.findTop1ByOrderByCreatedDateAsc()
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findByClientDeviceIdAndDataStatus(
        clientDeviceId: String,
        dataStatus: DataStatus,
        skipCache: Boolean
    ): AggregatorListResponse<GenericLogs> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.findByClientDeviceIdAndDataStatus(clientDeviceId, dataStatus)
        return AggregatorListResponse(
            data = ArrayList(resultFromDb),
            databaseAccessLogId = databaseAccessLogId
        )
    }

    override fun findByDataOrderByCreatedDateDesc(
        sessionTrackingId: String?,
        genericLogsIdParam: String?,
        userId: String?,
        userType: String?,
        apiTire: String?,
        clientDeviceId: String?,
        httpMethod: String?,
        api: String?,
        fromDate: ZonedDateTime?,
        toDate: ZonedDateTime?,
        dataStatus: DataStatus,
        skipCache: Boolean
    ): AggregatorListResponse<GenericLogs> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.findByDataOrderByCreatedDateDesc(
            sessionTrackingId = sessionTrackingId,
            apiAccessLogId = genericLogsIdParam,
            userId = userId,
            userType = userType,
            apiTire = apiTire,
            api = api,
            httpMethod = httpMethod,
            clientDeviceId = clientDeviceId,
            fromDate = fromDate,
            toDate = toDate,
            dataStatus = dataStatus,
            fromDateIsNull = fromDate == null,
            toDateIsNull = toDate == null
        )
        return AggregatorListResponse(
            data = ArrayList(resultFromDb),
            databaseAccessLogId = databaseAccessLogId
        )
    }

    override fun countByData(
        sessionTrackingId: String?,
        genericLogsIdParam: String?,
        userId: String?,
        userType: String?,
        apiTire: String?,
        clientDeviceId: String?,
        api: String?,
        httpMethod: String?,
        fromDate: ZonedDateTime?,
        toDate: ZonedDateTime?,
        dataStatus: DataStatus,
        skipCache: Boolean
    ): AggregatorResponse<Long> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.countByData(
            sessionTrackingId = sessionTrackingId,
            apiAccessLogId = genericLogsIdParam,
            userId = userId,
            userType = userType,
            apiTire = apiTire,
            api = api,
            httpMethod = httpMethod,
            clientDeviceId = clientDeviceId,
            fromDate = fromDate,
            toDate = toDate,
            dataStatus = dataStatus,
            fromDateIsNull = fromDate == null,
            toDateIsNull = toDate == null,
        )
        return AggregatorResponse(
            data = resultFromDb,
            databaseAccessLogId = databaseAccessLogId
        )
    }


    override fun findByDataWithOffsetId(
        sessionTrackingId: String?,
        genericLogsIdParam: String?,
        userId: String?,
        userType: String?,
        apiTire: String?,
        clientDeviceId: String?,
        api: String?,
        httpMethod: String?,
        fromDate: ZonedDateTime?,
        toDate: ZonedDateTime?,
        offsetId: String,
        offsetDate: ZonedDateTime,
        limit: Int,
        dataStatus: DataStatus,
        skipCache: Boolean
    ): AggregatorListResponse<GenericLogs> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.findByDataWithOffsetId(
            sessionTrackingId = sessionTrackingId,
            apiAccessLogId = genericLogsIdParam,
            userId = userId,
            userType = userType,
            apiTire = apiTire,
            api = api,
            httpMethod = httpMethod,
            clientDeviceId = clientDeviceId,
            fromDate = fromDate,
            toDate = toDate,
            dataStatus = dataStatus,
            offsetDate = offsetDate,
            offsetId = offsetId,
            limit = limit,
            fromDateIsNull = fromDate == null,
            toDateIsNull = toDate == null
        )
        return AggregatorListResponse(
            data = ArrayList(resultFromDb),
            databaseAccessLogId = databaseAccessLogId
        )
    }


    override fun findByDataWithOutOffsetId(
        sessionTrackingId: String?,
        genericLogsIdParam: String?,
        userId: String?,
        userType: String?,
        apiTire: String?,
        clientDeviceId: String?,
        api: String?,
        httpMethod: String?,
        fromDate: ZonedDateTime?,
        toDate: ZonedDateTime?,
        offsetDate: ZonedDateTime,
        limit: Int,
        dataStatus: DataStatus,
        skipCache: Boolean,
    ): AggregatorListResponse<GenericLogs> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.findByDataWithOutOffsetId(
            sessionTrackingId = sessionTrackingId,
            apiAccessLogId = genericLogsIdParam,
            userId = userId,
            userType = userType,
            apiTire = apiTire,
            api = api,
            httpMethod = httpMethod,
            clientDeviceId = clientDeviceId,
            fromDate = fromDate,
            toDate = toDate,
            dataStatus = dataStatus,
            offsetDate = offsetDate,
            limit = limit,
            fromDateIsNull = fromDate == null,
            toDateIsNull = toDate == null
        )
        return AggregatorListResponse(
            data = ArrayList(resultFromDb),
            databaseAccessLogId = databaseAccessLogId
        )
    }


    override fun findUniqueSessions(
        userId: String?,
        skipCache: Boolean
    ): AggregatorListResponse<GenericLogs> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.findUniqueSessions(
            userId = userId,
        )
        return AggregatorListResponse(
            data = ArrayList(resultFromDb),
            databaseAccessLogId = databaseAccessLogId
        )
    }

    override fun findAllApiLogGroupByApis(
        skipCache: Boolean,
    ): AggregatorListResponse<GenericLogs> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.findByDataStatusGroupByApis(
            dataStatus = DataStatus.ACTIVE,
        )
        return AggregatorListResponse(
            data = ArrayList(resultFromDb),
            databaseAccessLogId = databaseAccessLogId
        )
    }

    override fun findByUserIdAndSuccessFulAndDataStatus(
        userId: String,
        successFul: Boolean,
        dataStatus: DataStatus,
        skipCache: Boolean
    ): AggregatorListResponse<GenericLogs> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.findByUserIdAndSuccessfulAndDataStatus(
            userId, successFul, dataStatus
        )
        return AggregatorListResponse(
            data = ArrayList(resultFromDb),
            databaseAccessLogId = databaseAccessLogId
        )
    }

    override fun disableAllFailsForUserId(
        successFul: Boolean,
        userId: String,
        dataStatus: DataStatus
    ) {
        genericLogsRepo.disableAllFailsForUserId(successFul, userId, dataStatus)
    }

    override fun findTop1ByOrderByCreatedDateDesc(): AggregatorListResponse<FetchMostRecentMinifiedResponseImpl> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.findTop1ByOrderByCreatedDateDesc().map { toMap ->
            FetchMostRecentMinifiedResponseImpl(
                id = toMap.id,
                createdDate = toMap.createdDate
            )
        }
            .toCollection(ArrayList())
        return AggregatorListResponse(
            data = ArrayList(resultFromDb),
            databaseAccessLogId = databaseAccessLogId
        )
    }

    override fun findCountWithoutOffsetIdAndDate(
        userId: String?,
        treeCode: String,
        api: String?,
        applicationGroupCode: String?,
        applicationCode: String?,
        fromDateFinal: ZonedDateTime?,
        toDateFinal: ZonedDateTime?
    ): AggregatorResponse<Long> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.findCountWithoutOffsetIdAndDate(
            userId,
            considerUserId = userId == null,
            treeCode,
            api,
            considerApiIsNull = api == null,
            applicationGroupCode,
            considerAppGroupCode = applicationGroupCode == null,
            applicationCode,
            considerApplicationCode = applicationCode == null,
            fromDateFinal,
            considerFromDate = fromDateFinal == null,
            toDateFinal,
            considerToDate = toDateFinal == null,
            DataStatus.ACTIVE
        )
        return AggregatorResponse(
            data = resultFromDb,
            databaseAccessLogId = databaseAccessLogId
        )
    }

    override fun findDataWithOutOffsetIdAndDate(
        userId: String?,
        treeCode: String,
        api: String?,
        applicationGroupCode: String?,
        applicationCode: String?,
        fromDateFinal: ZonedDateTime?,
        toDateFinal: ZonedDateTime?
    ): AggregatorListResponse<GenericLogs> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.findDataWithOutOffsetIdAndDate(
            userId,
            considerUserId = userId == null,
            treeCode,
            api,
            considerApiIsNull = api == null,
            applicationGroupCode,
            considerAppGroupCode = applicationGroupCode == null,
            applicationCode,
            considerApplicationCode = applicationCode == null,
            fromDateFinal,
            considerFromDate = fromDateFinal == null,
            toDateFinal,
            considerToDate = toDateFinal == null,
            DataStatus.ACTIVE
        )
        return AggregatorListResponse(
            data = ArrayList(resultFromDb),
            databaseAccessLogId = databaseAccessLogId
        )
    }

    override fun findDataWithOutOffsetId(
        userId: String?,
        treeCode: String,
        api: String?,
        applicationGroupCode: String?,
        applicationCode: String?,
        fromDateFinal: ZonedDateTime?,
        toDateFinal: ZonedDateTime?,
        offsetDate: ZonedDateTime,
        limit: Int
    ): AggregatorListResponse<GenericLogs> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.findDataWithOutOffsetId(
            userId,
            considerUserId = userId == null,
            treeCode,
            api,
            considerApiIsNull = api == null,
            applicationGroupCode,
            considerAppGroupCode = applicationGroupCode == null,
            applicationCode,
            considerApplicationCode = applicationCode == null,
            fromDateFinal,
            considerFromDate = fromDateFinal == null,
            toDateFinal,
            considerToDate = toDateFinal == null,
            offsetDate,
            DataStatus.ACTIVE,
            limit
        )
        return AggregatorListResponse(
            data = ArrayList(resultFromDb),
            databaseAccessLogId = databaseAccessLogId
        )
    }

    override fun findDataWithOffsetId(
        userId: String?,
        treeCode: String,
        api: String?,
        applicationGroupCode: String?,
        applicationCode: String?,
        fromDateFinal: ZonedDateTime?,
        toDateFinal: ZonedDateTime?,
        offsetId: String,
        offsetDate: ZonedDateTime,
        limit: Int
    ): AggregatorListResponse<GenericLogs> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val resultFromDb = genericLogsRepo.findDataWithOffsetId(
            userId,
            considerUserId = userId == null,
            treeCode,
            api,
            considerApiIsNull = api == null,
            applicationGroupCode,
            considerAppGroupCode = applicationGroupCode == null,
            applicationCode,
            considerApplicationCode = applicationCode == null,
            fromDateFinal,
            considerFromDate = fromDateFinal == null,
            toDateFinal,
            considerToDate = toDateFinal == null,
            offsetId,
            offsetDate,
            DataStatus.ACTIVE,
            limit
        )
        return AggregatorListResponse(
            data = ArrayList(resultFromDb),
            databaseAccessLogId = databaseAccessLogId
        )
    }
}