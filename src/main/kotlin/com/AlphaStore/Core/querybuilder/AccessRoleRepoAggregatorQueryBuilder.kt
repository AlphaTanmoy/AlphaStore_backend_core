package com.alphaStore.Core.querybuilder

import com.alphaStore.Core.entity.AccessRole
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.enums.UserType
import org.springframework.stereotype.Component

@Component
class AccessRoleRepoAggregatorQueryBuilder(
    private val queryBuilderMaster: QueryBuilderMaster,
    private val entityAndTableNameMaster: EntityAndTableNameMaster
) {


    fun findByCodeAndDataStatusQueryBuilder(
        code: String,
        accessRole: AccessRole,
        userId: String,
        userType: UserType,
        dataStatus: DataStatus = DataStatus.ACTIVE
    ): String {
        val rawQueryBase = "SELECT * " +
                "FROM ${entityAndTableNameMaster.getFromEntityName(AccessRole::class.java.simpleName).table} "
        val rawQueryWhereClause = "WHERE code = '${code}' " +
                "AND data_status = '${dataStatus.name}' "
        val rawQueryOrderByAndLimit = ""
        return queryBuilderMaster.build(
            accessRole,
            userId,
            userType,
            entityAndTableNameMaster.getFromEntityName(AccessRole::class.java.simpleName),
            rawQueryBase,
            rawQueryWhereClause,
            rawQueryOrderByAndLimit
        )
    }
}