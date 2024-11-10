package com.alphaStore.Core.repo

import com.alphaStore.Core.entity.GenericLogs
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.minifiedresponse.FetchMostRecentMinifiedResponse
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.ZonedDateTime

@Suppress("SqlDialectInspection", "SqlNoDataSourceInspection")
interface GenericLogsRepo : JpaRepository<GenericLogs, String> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
        value = "DROP TABLE generic_logs CASCADE",
        nativeQuery = true
    )
    fun dropTable()

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
        value = "CREATE OR REPLACE FUNCTION execute_generic_logs_queries (query_to_execute IN VARCHAR) RETURNS SETOF generic_logs " +
                "AS " +
                "\$BODY\$ " +
                "DECLARE " +
                "BEGIN " +
                "RETURN QUERY EXECUTE query_to_execute; " +
                "END; " +
                "\$BODY\$ " +
                "LANGUAGE PLPGSQL;",
        nativeQuery = true
    )
    fun createQueryExecutionStoredFunction()

    @Query(
        value = "SELECT * FROM execute_generic_logs_queries(:queryToExecute)",
        nativeQuery = true
    )
    fun executeFunction(@Param("queryToExecute") queryToExecute: String): List<GenericLogs>

    fun findByDataStatus(dataStatus: DataStatus = DataStatus.ACTIVE): List<GenericLogs>

    fun findTop1ByOrderByCreatedDateAsc(): List<GenericLogs>

    @Query(
        value = "Select generic_logs.id as id, generic_logs.created_date as createdDate " +
                "FROM generic_logs " +
                "ORDER BY generic_logs.created_date DESC " +
                "LIMIT 1 ", nativeQuery = true
    )
    fun findTop1ByOrderByCreatedDateDesc(): List<FetchMostRecentMinifiedResponse>

    fun countByDataStatus(dataStatus: DataStatus = DataStatus.ACTIVE): Long

    @Query(
        value = "SELECT * " +
                "FROM generic_logs " +
                "WHERE client_device_id = :clientDeviceId " +
                "AND data_status = :#{#dataStatus.name()} " +
                "ORDER BY last_updated DESC " +
                "LIMIT 1",
        nativeQuery = true
    )
    fun findByClientDeviceIdAndDataStatus(
        @Param("clientDeviceId") clientDeviceId: String,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE
    ): List<GenericLogs>

    @Query(
        value = "SELECT generic_logs.id, generic_logs.created_date, generic_logs.data_status, " +
                "generic_logs.last_updated, api, api_tire, args, client_device_id, http_method, ip, is_auth_api, " +
                "session_tracking_id, user_id " +
                "FROM generic_logs " +
                "LEFT OUTER JOIN admin_users " +
                "ON admin_users.id = generic_logs.user_id " +
                "LEFT OUTER JOIN merchants " +
                "ON merchants.id = generic_logs.user_id " +
                "WHERE generic_logs.data_status = :#{#dataStatus.name()} " +
                "AND CASE WHEN :apiAccessLogId IS NULL THEN TRUE " +
                "ELSE generic_logs.id = :apiAccessLogId END " +
                "AND CASE WHEN :userType IS NULL THEN TRUE " +
                "ELSE ( " +
                "CASE WHEN :userType = 'ADMIN' " +
                "THEN admin_users.id IS NOT NULL " +
                "ELSE merchants.id IS NOT NULL END " +
                ") END " +
                "AND CASE WHEN :userId IS NULL THEN TRUE " +
                "ELSE user_id = :userId END " +
                "AND CASE WHEN :apiTire IS NULL THEN TRUE " +
                "ELSE api_tire = :apiTire END " +
                "AND CASE WHEN :clientDeviceId IS NULL THEN TRUE " +
                "ELSE client_device_id = :clientDeviceId END " +
                "AND CASE WHEN :api IS NULL THEN TRUE " +
                "ELSE api = :api END " +
                "AND CASE WHEN :httpMethod IS NULL THEN TRUE " +
                "ELSE http_method = :httpMethod END " +
                "AND CASE WHEN :sessionTrackingId IS NULL THEN TRUE " +
                "ELSE session_tracking_id = :sessionTrackingId END " +
                "AND CASE WHEN :fromDateIsNull THEN TRUE " +
                "ELSE generic_logs.created_date >= :fromDate END " +
                "AND CASE WHEN :toDateIsNull THEN TRUE " +
                "ELSE generic_logs.created_date <= :toDate END " +
                "ORDER BY created_date DESC LIMIT 1", nativeQuery = true
    )
    fun findByDataOrderByCreatedDateDesc(
        @Param("sessionTrackingId") sessionTrackingId: String? = null,
        @Param("apiAccessLogId") apiAccessLogId: String? = null,
        @Param("userId") userId: String? = null,
        @Param("userType") userType: String? = null,
        @Param("apiTire") apiTire: String? = null,
        @Param("clientDeviceId") clientDeviceId: String? = null,
        @Param("api") api: String? = null,
        @Param("fromDate") fromDate: ZonedDateTime? = null,
        @Param("httpMethod") httpMethod: String? = null,
        @Param("fromDateIsNull") fromDateIsNull: Boolean,
        @Param("toDate") toDate: ZonedDateTime? = null,
        @Param("toDateIsNull") toDateIsNull: Boolean,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
    ): List<GenericLogs>

    @Query(
        value = "SELECT COUNT(*) FROM generic_logs " +
                "LEFT OUTER JOIN admin_users " +
                "ON admin_users.id = generic_logs.user_id " +
                "LEFT OUTER JOIN merchants " +
                "ON merchants.id = generic_logs.user_id " +
                "WHERE generic_logs.data_status = :#{#dataStatus.name()} " +
                "AND CASE WHEN :apiAccessLogId IS NULL THEN TRUE " +
                "ELSE generic_logs.id = :apiAccessLogId END " +
                "AND CASE WHEN :userType IS NULL THEN TRUE " +
                "ELSE ( " +
                "CASE WHEN :userType = 'ADMIN' " +
                "THEN admin_users.id IS NOT NULL " +
                "ELSE merchants.id IS NOT NULL END " +
                ") END " +
                "AND CASE WHEN :userId IS NULL THEN TRUE " +
                "ELSE user_id = :userId END " +
                "AND CASE WHEN :apiTire IS NULL THEN TRUE " +
                "ELSE api_tire = :apiTire END " +
                "AND CASE WHEN :clientDeviceId IS NULL THEN TRUE " +
                "ELSE client_device_id = :clientDeviceId END " +
                "AND CASE WHEN :api IS NULL THEN TRUE " +
                "ELSE api = :api END " +
                "AND CASE WHEN :httpMethod IS NULL THEN TRUE " +
                "ELSE http_method = :httpMethod END " +
                "AND CASE WHEN :sessionTrackingId IS NULL THEN TRUE " +
                "ELSE session_tracking_id = :sessionTrackingId END " +
                "AND CASE WHEN :fromDateIsNull THEN TRUE " +
                "ELSE generic_logs.created_date >= :fromDate END " +
                "AND CASE WHEN :toDateIsNull THEN TRUE " +
                "ELSE generic_logs.created_date <= :toDate END ", nativeQuery = true
    )
    fun countByData(
        @Param("sessionTrackingId") sessionTrackingId: String? = null,
        @Param("apiAccessLogId") apiAccessLogId: String? = null,
        @Param("userId") userId: String? = null,
        @Param("userType") userType: String? = null,
        @Param("apiTire") apiTire: String? = null,
        @Param("clientDeviceId") clientDeviceId: String? = null,
        @Param("api") api: String? = null,
        @Param("httpMethod") httpMethod: String? = null,
        @Param("fromDate") fromDate: ZonedDateTime? = null,
        @Param("fromDateIsNull") fromDateIsNull: Boolean,
        @Param("toDate") toDate: ZonedDateTime? = null,
        @Param("toDateIsNull") toDateIsNull: Boolean,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
    ): Long

    @Query(
        value = "SELECT generic_logs.id, generic_logs.created_date, generic_logs.data_status, " +
                "generic_logs.last_updated, api, api_tire, args, client_device_id, http_method, ip, is_auth_api, " +
                "session_tracking_id, user_id " +
                "FROM generic_logs " +
                "LEFT OUTER JOIN admin_users " +
                "ON admin_users.id = generic_logs.user_id " +
                "LEFT OUTER JOIN merchants " +
                "ON merchants.id = generic_logs.user_id " +
                "WHERE generic_logs.id < :offsetId " +
                "AND generic_logs.created_date = :offsetDate " +
                "AND generic_logs.data_status = :#{#dataStatus.name()} " +
                "AND CASE WHEN :apiAccessLogId IS NULL THEN TRUE " +
                "ELSE generic_logs.id = :apiAccessLogId END " +
                "AND CASE WHEN :userType IS NULL THEN TRUE " +
                "ELSE ( " +
                "CASE WHEN :userType = 'ADMIN' " +
                "THEN admin_users.id IS NOT NULL " +
                "ELSE merchants.id IS NOT NULL END " +
                ") END " +
                "AND CASE WHEN :userId IS NULL THEN TRUE " +
                "ELSE user_id = :userId END " +
                "AND CASE WHEN :apiTire IS NULL THEN TRUE " +
                "ELSE api_tire = :apiTire END " +
                "AND CASE WHEN :clientDeviceId IS NULL THEN TRUE " +
                "ELSE client_device_id = :clientDeviceId END " +
                "AND CASE WHEN :api IS NULL THEN TRUE " +
                "ELSE api = :api END " +
                "AND CASE WHEN :httpMethod IS NULL THEN TRUE " +
                "ELSE http_method = :httpMethod END " +
                "AND CASE WHEN :sessionTrackingId IS NULL THEN TRUE " +
                "ELSE session_tracking_id = :sessionTrackingId END " +
                "AND CASE WHEN :fromDateIsNull THEN TRUE " +
                "ELSE generic_logs.created_date >= :fromDate END " +
                "AND CASE WHEN :toDateIsNull THEN TRUE " +
                "ELSE generic_logs.created_date <= :toDate END " +
                "ORDER BY created_date DESC,id DESC LIMIT :limit", nativeQuery = true
    )
    fun findByDataWithOffsetId(
        @Param("sessionTrackingId") sessionTrackingId: String? = null,
        @Param("apiAccessLogId") apiAccessLogId: String? = null,
        @Param("userId") userId: String? = null,
        @Param("userType") userType: String? = null,
        @Param("apiTire") apiTire: String? = null,
        @Param("clientDeviceId") clientDeviceId: String? = null,
        @Param("api") api: String? = null,
        @Param("httpMethod") httpMethod: String? = null,
        @Param("fromDate") fromDate: ZonedDateTime? = null,
        @Param("fromDateIsNull") fromDateIsNull: Boolean,
        @Param("toDate") toDate: ZonedDateTime? = null,
        @Param("toDateIsNull") toDateIsNull: Boolean,
        @Param("offsetId") offsetId: String = "",
        @Param("offsetDate") offsetDate: ZonedDateTime,
        @Param("limit") limit: Int,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
    ): List<GenericLogs>

    @Query(
        value = "SELECT generic_logs.id, generic_logs.created_date, generic_logs.data_status, " +
                "generic_logs.last_updated, api, api_tire, args, client_device_id, http_method, ip, is_auth_api, " +
                "session_tracking_id, user_id " +
                "FROM generic_logs " +
                "LEFT OUTER JOIN admin_users " +
                "ON admin_users.id = generic_logs.user_id " +
                "LEFT OUTER JOIN merchants " +
                "ON merchants.id = generic_logs.user_id " +
                "WHERE generic_logs.created_date < :offsetDate " +
                "AND generic_logs.data_status = :#{#dataStatus.name()} " +
                "AND CASE WHEN :apiAccessLogId IS NULL THEN TRUE " +
                "ELSE generic_logs.id = :apiAccessLogId END " +
                "AND CASE WHEN :userType IS NULL THEN TRUE " +
                "ELSE ( " +
                "CASE WHEN :userType = 'ADMIN' " +
                "THEN admin_users.id IS NOT NULL " +
                "ELSE merchants.id IS NOT NULL END " +
                ") END " +
                "AND CASE WHEN :userId IS NULL THEN TRUE " +
                "ELSE user_id = :userId END " +
                "AND CASE WHEN :apiTire IS NULL THEN TRUE " +
                "ELSE api_tire = :apiTire END " +
                "AND CASE WHEN :clientDeviceId IS NULL THEN TRUE " +
                "ELSE client_device_id = :clientDeviceId END " +
                "AND CASE WHEN :api IS NULL THEN TRUE " +
                "ELSE api = :api END " +
                "AND CASE WHEN :httpMethod IS NULL THEN TRUE " +
                "ELSE http_method = :httpMethod END " +
                "AND CASE WHEN :sessionTrackingId IS NULL THEN TRUE " +
                "ELSE session_tracking_id = :sessionTrackingId END " +
                "AND CASE WHEN :fromDateIsNull THEN TRUE " +
                "ELSE generic_logs.created_date >= :fromDate END " +
                "AND CASE WHEN :toDateIsNull THEN TRUE " +
                "ELSE generic_logs.created_date <= :toDate END " +
                "ORDER BY created_date DESC,id DESC LIMIT :limit", nativeQuery = true
    )
    fun findByDataWithOutOffsetId(
        @Param("sessionTrackingId") sessionTrackingId: String? = null,
        @Param("apiAccessLogId") apiAccessLogId: String? = null,
        @Param("userId") userId: String? = null,
        @Param("userType") userType: String? = null,
        @Param("apiTire") apiTire: String? = null,
        @Param("clientDeviceId") clientDeviceId: String? = null,
        @Param("api") api: String? = null,
        @Param("httpMethod") httpMethod: String? = null,
        @Param("fromDate") fromDate: ZonedDateTime? = null,
        @Param("fromDateIsNull") fromDateIsNull: Boolean,
        @Param("toDate") toDate: ZonedDateTime? = null,
        @Param("toDateIsNull") toDateIsNull: Boolean,
        @Param("offsetDate") offsetDate: ZonedDateTime,
        @Param("limit") limit: Int,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
    ): List<GenericLogs>

    @Query(
        value = "SELECT generic_logs.* " +
                "FROM generic_logs " +
                "INNER JOIN " +
                "( " +
                "SELECT min(created_date), session_tracking_id " +
                "FROM generic_logs " +
                "WHERE " +
                "CASE WHEN :userId IS NULL THEN TRUE " +
                "ELSE generic_logs.user_id = :userId END " +
                "GROUP BY session_tracking_id " +
                ") " +
                "AS res_one " +
                "ON res_one.min = generic_logs.created_date " +
                "AND res_one.session_tracking_id = generic_logs.session_tracking_id",
        nativeQuery = true
    )
    fun findUniqueSessions(
        @Param("userId") userId: String? = null,
    ): List<GenericLogs>

    @Query(
        value = "SELECT CAST(min(id) as varchar(255)) as id,min(created_date) as created_date,CAST(min(data_status) as VARCHAR(255)) as data_status," +
                "min(last_updated) as last_updated,CAST(min(api) as VARCHAR(255)) as api, " +
                "CAST(min(api_tire) as VARCHAR(255)) as api_tire, CAST(min(args) as VARCHAR(255)) as args," +
                "CAST(min(client_device_id) as VARCHAR(255)) as client_device_id, CAST(min(http_method) as VARCHAR(255)) as http_method, " +
                "CAST(min(ip) as VARCHAR(255)) as ip, bool_or(is_auth_api) as is_auth_api, " +
                "CAST(min(session_tracking_id) as VARCHAR(255)) as session_tracking_id, " +
                "CAST(min(user_id) as VARCHAR(255)) as user_id " +
                "FROM generic_logs " +
                "WHERE data_status = :#{#dataStatus.name()} " +
                "GROUP BY api",
        nativeQuery = true
    )
    fun findByDataStatusGroupByApis(dataStatus: DataStatus = DataStatus.ACTIVE): List<GenericLogs>

    fun findByUserIdAndSuccessfulAndDataStatus(
        userId: String,
        successFul: Boolean,
        dataStatus: DataStatus = DataStatus.ACTIVE
    ): List<GenericLogs>

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
        value = "UPDATE generic_logs " +
                "SET data_status = :#{#dataStatus.name()} " +
                "WHERE user_id = :userId " +
                "AND successful = :successFul",
        nativeQuery = true
    )
    fun disableAllFailsForUserId(
        @Param("successFul") successFul: Boolean,
        @Param("userId") userId: String,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.INACTIVE,
    )

    @Query(
        value = "SELECT COUNT(*) " +
                "FROM generic_logs " +
                "INNER JOIN users ON users.id = generic_logs.user_id " +
                "INNER JOIN application_groups ON users.id = application_groups.user_id " +
                "INNER JOIN applications ON application_groups.id = applications.application_group_id " +
                "WHERE users.data_status = :#{#dataStatus.name()} " +
                "AND users.tree_code LIKE :treeCode% " +
                "AND CASE WHEN :considerUserId THEN TRUE ELSE users.id = :userId END " +
                "AND CASE WHEN :considerApiIsNull THEN TRUE ELSE generic_logs.api = :api END " +
                "AND CASE WHEN :considerAppGroupCode THEN TRUE " +
                "ELSE application_groups.application_group_code = :applicationGroupCode END " +
                "AND CASE WHEN :considerApplicationCode THEN TRUE " +
                "ELSE applications.application_code = :applicationCode END " +
                "AND CASE WHEN :considerFromDate THEN TRUE ELSE generic_logs.created_date >= :fromDate END " +
                "AND CASE WHEN :considerToDate THEN TRUE ELSE generic_logs.created_date <= :toDate END ",
        nativeQuery = true
    )

    fun findCountWithoutOffsetIdAndDate(
        @Param("userId") userId: String? = null,
        @Param("considerUserId") considerUserId: Boolean,
        @Param("treeCode") treeCode: String,
        @Param("api") api: String? = null,
        @Param("considerApiIsNull") considerApiIsNull: Boolean,
        @Param("applicationGroupCode") applicationGroupCode: String? = null,
        @Param("considerAppGroupCode") considerAppGroupCode: Boolean,
        @Param("applicationCode") applicationCode: String? = null,
        @Param("considerApplicationCode") considerApplicationCode: Boolean,
        @Param("fromDate") fromDate: ZonedDateTime? = null,
        @Param("considerFromDate") considerFromDate: Boolean,
        @Param("toDate") toDate: ZonedDateTime? = null,
        @Param("considerToDate") considerToDate: Boolean,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE
    ): Long

    @Query(
        value = "SELECT generic_logs.* " +
                "FROM generic_logs " +
                "INNER JOIN users ON users.id = generic_logs.user_id " +
                "INNER JOIN application_groups ON users.id = application_groups.user_id " +
                "INNER JOIN applications ON application_groups.id = applications.application_group_id " +
                "WHERE users.data_status = :#{#dataStatus.name()} " +
                "AND users.tree_code LIKE :treeCode% " +
                "AND CASE WHEN :considerUserId THEN TRUE ELSE users.id = :userId END " +
                "AND CASE WHEN :considerApiIsNull THEN TRUE ELSE generic_logs.api = :api END " +
                "AND CASE WHEN :considerAppGroupCode THEN TRUE " +
                "ELSE application_groups.application_group_code = :applicationGroupCode END " +
                "AND CASE WHEN :considerApplicationCode THEN TRUE " +
                "ELSE applications.application_code = :applicationCode END " +
                "AND CASE WHEN :considerFromDate THEN TRUE ELSE generic_logs.created_date >= :fromDate END " +
                "AND CASE WHEN :considerToDate THEN TRUE ELSE generic_logs.created_date <= :toDate END " +
                "ORDER BY generic_logs.created_date DESC ",
        nativeQuery = true
    )
    fun findDataWithOutOffsetIdAndDate(
        @Param("userId") userId: String? = null,
        @Param("considerUserId") considerUserId: Boolean,
        @Param("treeCode") treeCode: String,
        @Param("api") api: String? = null,
        @Param("considerApiIsNull") considerApiIsNull: Boolean,
        @Param("applicationGroupCode") applicationGroupCode: String? = null,
        @Param("considerAppGroupCode") considerAppGroupCode: Boolean,
        @Param("applicationCode") applicationCode: String? = null,
        @Param("considerApplicationCode") considerApplicationCode: Boolean,
        @Param("fromDate") fromDate: ZonedDateTime? = null,
        @Param("considerFromDate") considerFromDate: Boolean,
        @Param("toDate") toDate: ZonedDateTime? = null,
        @Param("considerToDate") considerToDate: Boolean,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE
    ): List<GenericLogs>

    @Query(
        value = "SELECT generic_logs.* " +
                "FROM generic_logs " +
                "INNER JOIN users ON users.id = generic_logs.user_id " +
                "INNER JOIN application_groups ON users.id = application_groups.user_id " +
                "INNER JOIN applications ON application_groups.id = applications.application_group_id " +
                "WHERE generic_logs.created_date < :offsetDate " +
                "AND users.data_status = :#{#dataStatus.name()} " +
                "AND users.tree_code LIKE :treeCode% " +
                "AND CASE WHEN :considerUserId THEN TRUE ELSE users.id = :userId END " +
                "AND CASE WHEN :considerApiIsNull THEN TRUE ELSE generic_logs.api = :api END " +
                "AND CASE WHEN :considerAppGroupCode THEN TRUE " +
                "ELSE application_groups.application_group_code = :applicationGroupCode END " +
                "AND CASE WHEN :considerApplicationCode THEN TRUE " +
                "ELSE applications.application_code = :applicationCode END " +
                "AND CASE WHEN :considerFromDate THEN TRUE ELSE generic_logs.created_date >= :fromDate END " +
                "AND CASE WHEN :considerToDate THEN TRUE ELSE generic_logs.created_date <= :toDate END " +
                "ORDER BY generic_logs.created_date DESC " +
                "LIMIT :limit ",
        nativeQuery = true
    )
    fun findDataWithOutOffsetId(
        @Param("userId") userId: String? = null,
        @Param("considerUserId") considerUserId: Boolean,
        @Param("treeCode") treeCode: String,
        @Param("api") api: String? = null,
        @Param("considerApiIsNull") considerApiIsNull: Boolean,
        @Param("applicationGroupCode") applicationGroupCode: String? = null,
        @Param("considerAppGroupCode") considerAppGroupCode: Boolean,
        @Param("applicationCode") applicationCode: String? = null,
        @Param("considerApplicationCode") considerApplicationCode: Boolean,
        @Param("fromDate") fromDate: ZonedDateTime? = null,
        @Param("considerFromDate") considerFromDate: Boolean,
        @Param("toDate") toDate: ZonedDateTime? = null,
        @Param("considerToDate") considerToDate: Boolean,
        @Param("offsetDate") offsetDate: ZonedDateTime,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
        @Param("limit") limit: Int
    ): List<GenericLogs>

    @Query(
        value = "SELECT generic_logs.* " +
                "FROM generic_logs " +
                "INNER JOIN users ON users.id = generic_logs.user_id " +
                "INNER JOIN application_groups ON users.id = application_groups.user_id " +
                "INNER JOIN applications ON application_groups.id = applications.application_group_id " +
                "WHERE generic_logs.id < :offsetId " +
                "AND generic_logs.created_date = :offsetDate " +
                "AND users.data_status = :#{#dataStatus.name()} " +
                "AND users.tree_code LIKE :treeCode% " +
                "AND CASE WHEN :considerUserId THEN TRUE ELSE users.id = :userId END " +
                "AND CASE WHEN :considerApiIsNull THEN TRUE ELSE generic_logs.api = :api END " +
                "AND CASE WHEN :considerAppGroupCode THEN TRUE " +
                "ELSE application_groups.application_group_code = :applicationGroupCode END " +
                "AND CASE WHEN :considerApplicationCode THEN TRUE " +
                "ELSE applications.application_code = :applicationCode END " +
                "AND CASE WHEN :considerFromDate THEN TRUE ELSE generic_logs.created_date >= :fromDate END " +
                "AND CASE WHEN :considerToDate THEN TRUE ELSE generic_logs.created_date <= :toDate END " +
                "ORDER BY generic_logs.created_date DESC " +
                "LIMIT :limit ",
        nativeQuery = true
    )
    fun findDataWithOffsetId(
        @Param("userId") userId: String? = null,
        @Param("considerUserId") considerUserId: Boolean,
        @Param("treeCode") treeCode: String,
        @Param("api") api: String? = null,
        @Param("considerApiIsNull") considerApiIsNull: Boolean,
        @Param("applicationGroupCode") applicationGroupCode: String? = null,
        @Param("considerAppGroupCode") considerAppGroupCode: Boolean,
        @Param("applicationCode") applicationCode: String? = null,
        @Param("considerApplicationCode") considerApplicationCode: Boolean,
        @Param("fromDate") fromDate: ZonedDateTime? = null,
        @Param("considerFromDate") considerFromDate: Boolean,
        @Param("toDate") toDate: ZonedDateTime? = null,
        @Param("considerToDate") considerToDate: Boolean,
        @Param("offsetId") offsetId: String,
        @Param("offsetDate") offsetDate: ZonedDateTime,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
        @Param("limit") limit: Int
    ): List<GenericLogs>
}