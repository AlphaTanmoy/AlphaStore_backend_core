package com.alphaStore.Core.contracts

import com.alphaStore.Core.entity.AccessRole
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.enums.UserType
import com.alphaStore.Core.minifiedresponseimpl.AccessRoleMinifiedResponseImpl
import com.alphaStore.Core.minifiedresponseimpl.FetchMostRecentMinifiedResponseImpl
import com.alphaStore.Core.model.AggregatorListResponse
import com.alphaStore.Core.model.AggregatorResponse
import java.time.ZonedDateTime

interface AccessRoleRepoAggregatorContract {

    fun save(entity: AccessRole): AccessRole

    fun executeFunction(queryToExecute: String): List<AccessRole>
    fun saveAll(entities: List<AccessRole>)

    fun dropTable()

    fun findByIdAndDataStatus(
        id: String,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorListResponse<AccessRole>

    fun findByCodeAndDataStatus(
        code: String,
        accessRole: AccessRole? = null,
        userId: String? = null,
        userType: UserType? = null,
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorListResponse<AccessRole>

    fun countByDataStatus(
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorResponse<Long>

    fun findByDataStatus(
        dataStatus: DataStatus = DataStatus.ACTIVE,
        skipCache: Boolean = false,
    ): AggregatorListResponse<AccessRole>

    fun getAccessRoleWithTreeCode(
        treeCode: String,
        skipCache: Boolean = false,
    ): AggregatorListResponse<AccessRole>

    fun getAllChildrenAccessRoles(
        tweeCodeCondition: String,
        skipCache: Boolean = false,
    ): AggregatorListResponse<AccessRole>

    fun getHorizontalSiblingsCount(
        treeCodePatternToCheck: String,
        currentLevel: Int,
        skipCache: Boolean = false,
    ): AggregatorResponse<Long>



    fun findTop1ByOrderByCreatedDateAsc(
        skipCache: Boolean = false
    ): AggregatorListResponse<FetchMostRecentMinifiedResponseImpl>

    fun findCountWithOutOffsetIdOffsetDateAndLimit(
        userId: String,
        queryString: String,
        skipCache: Boolean = false,
    ): AggregatorResponse<Long>


    fun findAllDataWithOutOffsetIdOffsetDateAndLimit(
        userId: String,
        queryString: String,
        skipCache: Boolean = false,
    ): AggregatorListResponse<AccessRoleMinifiedResponseImpl>

    fun findWithOutOffsetId(
        userId: String,
        queryString: String,
        zonedDateTime: ZonedDateTime,
        limit: Int,
        skipCache: Boolean = false,
    ): AggregatorListResponse<AccessRoleMinifiedResponseImpl>

    fun findWithOffsetId(
        userId: String,
        queryString: String,
        offsetDate: ZonedDateTime,
        offsetId: String,
        limit: Int,
        skipCache: Boolean = false,
    ): AggregatorListResponse<AccessRoleMinifiedResponseImpl>
}