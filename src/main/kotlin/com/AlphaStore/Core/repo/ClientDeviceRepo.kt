package com.alphaStore.Core.repo

import com.alphaStore.Core.entity.ClientDevice
import com.alphaStore.Core.enums.DataStatus
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.ZonedDateTime

interface ClientDeviceRepo : JpaRepository<ClientDevice, String> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
        value = "DROP TABLE client_devices CASCADE",
        nativeQuery = true
    )
    fun dropTable()

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
        value = "CREATE OR REPLACE FUNCTION execute_client_devices_queries (query_to_execute IN VARCHAR) RETURNS SETOF client_devices " +
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
        value = "SELECT * FROM execute_client_devices_queries(:queryToExecute)",
        nativeQuery = true
    )
    fun executeFunction(@Param("queryToExecute") queryToExecute: String): List<ClientDevice>

    fun findByIdAndDataStatus(id: String, dataStatus: DataStatus = DataStatus.ACTIVE): List<ClientDevice>

    fun findByFcmIdAndFcmIdBeingUsedElseWhereAndDataStatus(
        fcmId: String,
        fcmIdBeingUsedElseWhere: Boolean = true,
        dataStatus: DataStatus
    ): List<ClientDevice>

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE client_devices " +
                "SET active = false " +
                "WHERE id NOT SIMILAR TO :negativeSimilarToCondition " +
                "AND active = true",
        nativeQuery = true
    )
    fun deactivateClientDevices(
        @Param("negativeSimilarToCondition") negativeSimilarToCondition: String,
    )

    @Query(
        value = "SELECT * " +
                "FROM client_devices " +
                "WHERE id SIMILAR TO :similarToCondition ",
        nativeQuery = true
    )
    fun getDevicesToMarkAsActive(
        @Param("similarToCondition") similarToCondition: String,
    ): List<ClientDevice>

    @Query(
        value = "SELECT * FROM client_devices " +
                "WHERE data_status = :#{#dataStatus.name()} " +
                "AND ( user_id = :userId " +
                ") " +
                "ORDER BY created_date ASC LIMIT 1", nativeQuery = true
    )
    fun findByDataOrderByCreatedDateAsc(
        @Param("userId") userId: String,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
    ): List<ClientDevice>

    @Query(
        value = "SELECT COUNT(*) FROM client_devices " +
                "WHERE data_status = :#{#dataStatus.name()} " +
                "AND ( user_id = :userId " +
                ") ", nativeQuery = true
    )
    fun countByData(
        @Param("userId") userId: String,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
    ): Long

    @Query(
        value = "SELECT * FROM client_devices " +
                "WHERE id > :offsetId " +
                "AND created_date = :offsetDate " +
                "AND data_status = :#{#dataStatus.name()} " +
                "AND ( user_id = :userId " +
                ") " +
                "ORDER BY created_date ASC,id ASC LIMIT :limit", nativeQuery = true
    )
    fun findByDataWithOffsetId(
        @Param("userId") userId: String,
        @Param("offsetId") offsetId: String = "",
        @Param("offsetDate") offsetDate: ZonedDateTime,
        @Param("limit") limit: Int,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
    ): List<ClientDevice>

    @Query(
        value = "SELECT * FROM client_devices " +
                "WHERE created_date > :offsetDate " +
                "AND data_status = :#{#dataStatus.name()} " +
                "AND ( user_id = :userId " +
                ") " +
                "ORDER BY created_date ASC,id ASC LIMIT :limit", nativeQuery = true
    )
    fun findByDataWithOutOffsetId(
        @Param("userId") userId: String,
        @Param("offsetDate") offsetDate: ZonedDateTime,
        @Param("limit") limit: Int,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
    ): List<ClientDevice>

    @Query(
        value = "SELECT * FROM client_devices " +
                "WHERE data_status = :#{#dataStatus.name()} " +
                "AND id = :id " +
                "AND CASE WHEN :userId IS NULL THEN TRUE ELSE user_id = :userId END ",
        nativeQuery = true
    )
    fun findByIdAndUserIdAndDataStatus(
        @Param("id") id: String,
        @Param("userId") userId: String? = null,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE
    ): List<ClientDevice>

    @Query(
        value = "SELECT * FROM client_devices " +
                "WHERE " +
                "(unique_identifier_id = :uniqueIdentifierId) " +
                "AND CASE WHEN :lookForUser " +
                "THEN user_id = :userId ELSE FALSE END " +
                "AND data_status = :#{#dataStatus.name()}",
        nativeQuery = true
    )
    fun findByUniqueIdentifierIdAndAdminIdOrCustomerIdAndDataStatus(
        @Param("uniqueIdentifierId") uniqueIdentifierId: String,
        @Param("userId") userId: String,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
        @Param("lookForUser") lookForUser:Boolean = false
    ): List<ClientDevice>
}