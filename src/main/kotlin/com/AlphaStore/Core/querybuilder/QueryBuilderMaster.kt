package com.alphaStore.Core.querybuilder

import com.alphaStore.Core.entity.AccessRole
import com.alphaStore.Core.enums.AccessRoleType
import com.alphaStore.Core.enums.UserType
import com.alphaStore.Core.model.EntityAndTableName
import org.springframework.stereotype.Component

@Component
class QueryBuilderMaster {

    fun build(
        accessRole: AccessRole,
        userId: String,
        userType: UserType,
        forEntity: EntityAndTableName,
        rawQueryBase: String,
        rawQueryWhereClause: String,
        rawQueryOrderByAndLimit: String
    ): String {
        val accessRoleType = accessRole.accessRoleType
        var includeList = mutableListOf<String>()
        var excludeList = mutableListOf<String>()

        when (accessRoleType) {
            AccessRoleType.ADMIN -> includeList = mutableListOf("field1", "field2")
            AccessRoleType.USER -> excludeList = mutableListOf("field3", "field4")
            else -> {}
        }

        if (includeList.isNotEmpty()) {
            includeList.removeAll(excludeList)
            var joinStatement = ""
            var paramsStatement = ""

            includeList.forEach { includeItem ->
                joinStatement += " JOIN $includeItem "
                paramsStatement += " AND $includeItem = '${accessRole.title}' "
            }

            val rawQueryWhereClauseFinal = if (rawQueryWhereClause.isEmpty()) "WHERE 1 = 1 " else rawQueryWhereClause
            return "$rawQueryBase$joinStatement$rawQueryWhereClauseFinal$paramsStatement$rawQueryOrderByAndLimit"

        } else {
            var joinStatement = ""
            var paramsStatement = ""

            excludeList.forEach { excludeItem ->
                joinStatement += " LEFT JOIN $excludeItem "
                paramsStatement += " AND $excludeItem != '${accessRole.title}' "
            }

            val rawQueryWhereClauseFinal = if (rawQueryWhereClause.isEmpty()) "WHERE 1 = 1 " else rawQueryWhereClause
            return "$rawQueryBase$joinStatement$rawQueryWhereClauseFinal$paramsStatement$rawQueryOrderByAndLimit"
        }
    }

    private fun getRawQueryBuildForParamValue(userId: String, userType: UserType, rawQueryTemplate: String): String {
        return rawQueryTemplate.replace(":id", userId)
    }
}
