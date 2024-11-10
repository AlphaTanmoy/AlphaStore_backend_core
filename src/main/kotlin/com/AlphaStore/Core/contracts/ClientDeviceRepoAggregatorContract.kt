package com.alphaStore.Core.contracts

import com.alphaStore.Core.entity.AccessRole
import com.alphaStore.Core.entity.ClientDevice
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.enums.UserType
import com.alphaStore.Core.model.AggregatorListResponse
import com.alphaStore.Core.model.AggregatorResponse
import java.time.ZonedDateTime

interface ClientDeviceRepoAggregatorContract {


    fun dropTable()

    fun executeFunction(queryToExecute: String): List<ClientDevice>

    fun saveAll(clientDevices: ArrayList<ClientDevice>)

    fun save(clientDevice: ClientDevice)

    fun findClientDevicesWithSameFcmIdsWhichIsNotBeingUsedElseWhere(
        fcmId: String,
        userId: String? = null,
        accessRole: AccessRole? = null,
        userType: UserType? = null,

        skipCache: Boolean = false,
    ): AggregatorListResponse<ClientDevice>

    fun findByIdAndDataStatus(
        deviceId: String,
    ): AggregatorListResponse<ClientDevice>

    fun deactivateClientDevices(
        negativeSimilarToCondition: String
    )

    fun getDevicesToMarkAsActive(
        similarToCondition: String,

        skipCache: Boolean = false,
    ): AggregatorListResponse<ClientDevice>

    fun findByDataOrderByCreatedDateAsc(
        userId: String,
        dataStatus: DataStatus = DataStatus.ACTIVE,

        skipCache: Boolean = false,
    ): AggregatorListResponse<ClientDevice>

    fun countByData(
        userId: String,
        dataStatus: DataStatus = DataStatus.ACTIVE,

        skipCache: Boolean = false,
    ): AggregatorResponse<Long>


    fun findByDataWithOffsetId(
        userId: String,
        offsetId: String = "",
        offsetDate: ZonedDateTime,
        limit: Int,
        dataStatus: DataStatus = DataStatus.ACTIVE,

        skipCache: Boolean = false,
    ): AggregatorListResponse<ClientDevice>


    fun findByDataWithOutOffsetId(
        userId: String,
        offsetDate: ZonedDateTime,
        limit: Int,
        dataStatus: DataStatus = DataStatus.ACTIVE,

        skipCache: Boolean = false,
    ): AggregatorListResponse<ClientDevice>

    fun findByIdAndUserIdAndDataStatus(
        id: String,
        merchantId: String? = null,
        dataStatus: DataStatus = DataStatus.ACTIVE,

        skipCache: Boolean = false,
    ): AggregatorListResponse<ClientDevice>

    fun findByUniqueIdentifierIdAndAdminIdOrCustomerIdAndDataStatus(
        uniqueIdentifierId: String,
        userId: String,
        accessRole: AccessRole? = null,
        userType: UserType? = null,
        dataStatus: DataStatus = DataStatus.ACTIVE,

        skipCache: Boolean = false,
    ): AggregatorListResponse<ClientDevice>

    fun findByUniqueIdentifierIdAndAdminUserIdAndDataStatus(
        uniqueIdentifierId: String,
        userId: String,
        accessRole: AccessRole? = null,
        userType: UserType? = null,
        dataStatus: DataStatus = DataStatus.ACTIVE,

        skipCache: Boolean = false,
    ): AggregatorListResponse<ClientDevice>
}