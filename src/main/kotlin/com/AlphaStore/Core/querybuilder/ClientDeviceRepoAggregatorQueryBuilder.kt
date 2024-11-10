package com.alphaStore.Core.querybuilder

import com.alphaStore.Core.entity.AccessRole
import com.alphaStore.Core.entity.ClientDevice
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.enums.UserType
import org.springframework.stereotype.Component

@Component
class ClientDeviceRepoAggregatorQueryBuilder(
    private val queryBuilderMaster: QueryBuilderMaster,
    private val entityAndTableNameMaster: EntityAndTableNameMaster
) {


    fun findClientDevicesWithSameFcmIdsWhichIsNotBeingUsedElseWhereQueryBuilder(
        fcmId: String,
        fcmIdBeingUsedElseWhere: Boolean,
        accessRole: AccessRole,
        userId: String,
        userType: UserType,
        dataStatus: DataStatus = DataStatus.ACTIVE
    ): String {
        val rawQueryBase =
            "SELECT * " +
                    "FROM ${entityAndTableNameMaster.getFromEntityName(ClientDevice::class.java.simpleName).table} "
        val rawQueryWhereClause = "WHERE fcm_id = '$fcmId' " +
                "AND fcm_id_being_used_else_where = '$fcmIdBeingUsedElseWhere' " +
                "AND data_status = '${dataStatus.name}' "
        val rawQueryOrderByAndLimit = ""
        return queryBuilderMaster.build(
            accessRole,
            userId,
            userType,
            entityAndTableNameMaster.getFromEntityName(ClientDevice::class.java.simpleName),
            rawQueryBase,
            rawQueryWhereClause,
            rawQueryOrderByAndLimit
        )
    }

    fun findByUniqueIdentifierIdAndAdminIdOrCustomerIdAndDataStatus(
        uniqueIdentifierId: String,
        userId: String,
        accessRole: AccessRole,
        usedByUserId: String,
        userType: UserType,
        dataStatus: DataStatus = DataStatus.ACTIVE
    ): String {
        val rawQueryBase =
            "SELECT * " +
                    "FROM ${entityAndTableNameMaster.getFromEntityName(ClientDevice::class.java.simpleName).table} "
        var rawQueryWhereClause = "WHERE unique_identifier_id = '$uniqueIdentifierId' " +
                "AND data_status = '${dataStatus.name}' "
        rawQueryWhereClause += "AND user_id = '$userId' "

        val rawQueryOrderByAndLimit = ""
        return queryBuilderMaster.build(
            accessRole,
            usedByUserId,
            userType,
            entityAndTableNameMaster.getFromEntityName(ClientDevice::class.java.simpleName),
            rawQueryBase,
            rawQueryWhereClause,
            rawQueryOrderByAndLimit
        )
    }

    fun findByUniqueIdentifierIdAndAdminUserIdAndDataStatusQueryBuilder(
        uniqueIdentifierId: String,
        userId: String,
        accessRole: AccessRole,
        usedByUserId: String,
        userType: UserType,
        dataStatus: DataStatus = DataStatus.ACTIVE
    ): String {
        val rawQueryBase =
            "SELECT * " +
                    "FROM ${entityAndTableNameMaster.getFromEntityName(ClientDevice::class.java.simpleName).table} "
        val rawQueryWhereClause = "WHERE unique_identifier_id = '$uniqueIdentifierId' " +
                "AND user_id = '$userId' " +
                "AND data_status = '${dataStatus.name}' "
        val rawQueryOrderByAndLimit = ""
        return queryBuilderMaster.build(
            accessRole,
            usedByUserId,
            userType,
            entityAndTableNameMaster.getFromEntityName(ClientDevice::class.java.simpleName),
            rawQueryBase,
            rawQueryWhereClause,
            rawQueryOrderByAndLimit
        )
    }
}