package com.alphaStore.Core.repo

import com.alphaStore.Core.entity.AccessRole
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.minifiedresponse.AccessRoleMinifiedResponse
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.ZonedDateTime
import java.util.*


@Suppress("SqlDialectInspection", "SqlNoDataSourceInspection")
interface AccessRoleRepo : JpaRepository<AccessRole, String> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
        value = "DROP TABLE access_roles CASCADE",
        nativeQuery = true
    )
    fun dropTable()

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
        value = "CREATE OR REPLACE FUNCTION execute_access_roles_queries (query_to_execute IN VARCHAR) RETURNS SETOF access_roles " +
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
        value = "SELECT * FROM execute_access_roles_queries(:queryToExecute)",
        nativeQuery = true
    )
    fun executeFunction(@Param("queryToExecute") queryToExecute: String): List<AccessRole>

    fun findByCodeAndDataStatus(
        code: String,
        dataStatus: DataStatus = DataStatus.ACTIVE
    ): List<AccessRole>

    fun findByDataStatus(dataStatus: DataStatus = DataStatus.ACTIVE): List<AccessRole>

    fun findTop1ByOrderByCreatedDateAsc(): List<AccessRole>

    fun findTop1ByOrderByCreatedDateDesc(): List<AccessRole>

    fun findByIdAndDataStatus(id: String, dataStatus: DataStatus = DataStatus.ACTIVE): List<AccessRole>

    fun countByDataStatus(dataStatus: DataStatus = DataStatus.ACTIVE): Long

    @Query(
        value = "SELECT * " +
                "FROM access_roles " +
                "WHERE tree_code = :treeCode ",
        nativeQuery = true
    )
    fun getAccessRoleWithTreeCode(
        @Param("treeCode") treeCode: String
    ): List<AccessRole>

    @Query(
        value = "SELECT * " +
                "FROM access_roles " +
                "WHERE tree_code like :tweeCodeCondition ",
        nativeQuery = true
    )
    fun getAllChildrenAccessRoles(
        @Param("tweeCodeCondition") tweeCodeCondition: String
    ): List<AccessRole>

    @Query(
        value = "SELECT " +
                "number_of_element FROM " +
                "( " +
                "SELECT " +
                "count as level, " +
                "count(*) as number_of_element " +
                "FROM " +
                "( " +
                "SELECT tree_code, " +
                "count(*) " +
                "FROM " +
                "( " +
                "SELECT " +
                "*, " +
                "unnest(string_to_array(tree_code,'-')) as tree_code_unnest " +
                "FROM " +
                "( " +
                "SELECT * FROM access_roles where tree_code like :treeCodePatternToCheck " +
                ") " +
                "as res " +
                ") " +
                "as res_two " +
                "GROUP BY tree_code " +
                ") " +
                "as res_three " +
                "GROUP BY count " +
                ") " +
                "as res_four " +
                "WHERE level = :currentLevel ",
        nativeQuery = true
    )
    fun getHorizontalSiblingsCount(
        @Param("treeCodePatternToCheck") treeCodePatternToCheck: String,
        @Param("currentLevel") currentLevel: Int
    ): Optional<Long>

    @Query(
        value = "SELECT COUNT(*)"+
                "FROM access_roles ar " +
                "WHERE ar.data_status = :#{#dataStatus.name()} " +
                "AND (" +
                "ar.id SIMILAR TO :queryString " +
                "OR u.access_role_id SIMILAR TO :queryString " +
                "OR u.name SIMILAR TO :queryString "+
                ")"

        , nativeQuery = true
    )
    fun findCountWithOutOffsetIdOffsetDateAndLimit(
        @Param("queryString") queryString: String,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE
    ): Long


    @Query(
        value = "SELECT "+
                "CAST(ar.id AS VARCHAR) AS id, " +
                "CAST(ar.code AS VARCHAR) AS code, " +
                "CAST(ar.title AS VARCHAR) AS title, " +
                "CAST(ar.description AS VARCHAR) AS description, " +
                "ar.created_date AS createdDate " +
                "WHERE ar.data_status = :#{#dataStatus.name()} " +
                "AND (" +
                "ar.id SIMILAR TO :queryString " +
                "OR u.access_role_id SIMILAR TO :queryString " +
                "OR u.name SIMILAR TO :queryString "+
                ") " +
                "ORDER BY ar.created_date ASC, ar.id ASC"
        , nativeQuery = true
    )
    fun findAllDataWithOutOffsetIdOffsetDateAndLimit(
        @Param("queryString") queryString: String,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE
    ): List<AccessRoleMinifiedResponse>


    @Query(
        value = "SELECT "+
                "CAST(ar.id AS VARCHAR) AS id, " +
                "CAST(ar.code AS VARCHAR) AS code, " +
                "CAST(ar.title AS VARCHAR) AS title, " +
                "CAST(ar.description AS VARCHAR) AS description, " +
                "ar.created_date AS createdDate " +
                "FROM access_roles ar " +
                "WHERE ar.created_date > :offsetDate " +
                "AND ar.data_status = :#{#dataStatus.name()} " +
                "AND ar.created_date > :offsetDate "+
                "AND (" +
                "ar.id SIMILAR TO :queryString " +
                "OR u.access_role_id SIMILAR TO :queryString " +
                "OR u.name SIMILAR TO :queryString "+
                ")" +
                "ORDER BY ar.created_date ASC,ar.id ASC "+
                "LIMIT :limit "
        , nativeQuery = true
    )
    fun findWithOutOffsetId(
        @Param("queryString") queryString: String,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
        @Param("offsetDate") offsetDate:ZonedDateTime,
        @Param("limit") limit: Int
    ): List<AccessRoleMinifiedResponse>


    @Query(
        value = "SELECT "+
                "CAST(ar.id AS VARCHAR) AS id, " +
                "CAST(ar.title AS VARCHAR) AS title, " +
                "CAST(ar.description AS VARCHAR) AS description, " +
                "ar.created_date AS createdDate " +
                "FROM access_roles ar " +
                "WHERE ar.access_role_id > :offsetId " +
                "AND ar.created_date = :offsetDate " +
                "AND ar.data_status = :#{#dataStatus.name()} " +
                "AND (" +
                "ar.id SIMILAR TO :queryString " +
                "OR u.access_role_id SIMILAR TO :queryString " +
                "OR u.name SIMILAR TO :queryString " +
                ")" +
                "ORDER BY ar.created_date ASC,ar.id ASC "+
                "LIMIT :limit "
        , nativeQuery = true
    )
    fun findWithOffsetId(
        @Param("queryString") queryString: String,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE,
        @Param("offsetId") offsetId: String,
        @Param("offsetDate") offsetDate:ZonedDateTime,
        @Param("limit") limit: Int
    ): List<AccessRoleMinifiedResponse>
}