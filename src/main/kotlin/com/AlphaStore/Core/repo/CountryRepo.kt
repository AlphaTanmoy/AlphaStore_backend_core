package com.alphaStore.Core.repo

import com.alphaStore.Core.entity.Country
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.minifiedresponse.CountryListMinifiedResponse
import com.alphaStore.Core.minifiedresponse.FetchMostRecentMinifiedResponse
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.ZonedDateTime

@Suppress("SqlDialectInspection", "SqlNoDataSourceInspection")
interface CountryRepo : JpaRepository<Country, String> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
        value = "DROP TABLE countries CASCADE",
        nativeQuery = true
    )
    fun dropTable()

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
        value = "CREATE OR REPLACE FUNCTION execute_countries_queries (query_to_execute IN VARCHAR) RETURNS SETOF countries " +
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

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
        value = "CREATE OR REPLACE FUNCTION execute_truncate_all () RETURNS int " +
                "AS " +
                "\$BODY\$ " +
                "DECLARE " +
                "BEGIN " +
                /*"EXECUTE 'TRUNCATE access_control_categories CASCADE'; " +
                "EXECUTE 'TRUNCATE access_control_categories_access_control_categories CASCADE'; " +
                "EXECUTE 'TRUNCATE access_control_categories_access_controls CASCADE'; " +
                "EXECUTE 'TRUNCATE access_control_category_orders_for_admin_users CASCADE'; " +
                "EXECUTE 'TRUNCATE access_control_policies CASCADE'; " +
                "EXECUTE 'TRUNCATE access_control_policy_param_entity_element_excludes CASCADE'; " +
                "EXECUTE 'TRUNCATE access_control_policy_param_row_includes CASCADE'; " +
                "EXECUTE 'TRUNCATE access_controls CASCADE'; " +
                "EXECUTE 'TRUNCATE access_role_datas CASCADE'; " +
                "EXECUTE 'TRUNCATE access_role_entity_column_control_includes CASCADE'; " +
                "EXECUTE 'TRUNCATE access_role_entity_column_controls CASCADE'; " +
                "EXECUTE 'TRUNCATE access_role_entity_row_control_excludes CASCADE'; " +
                "EXECUTE 'TRUNCATE access_role_entity_row_controls CASCADE'; " +
                "EXECUTE 'TRUNCATE access_roles CASCADE'; " +
                "EXECUTE 'TRUNCATE admin_past_passwords CASCADE'; " +
                "EXECUTE 'TRUNCATE admin_users CASCADE'; " +
                "EXECUTE 'TRUNCATE apis_access_logs CASCADE'; " +
                "EXECUTE 'TRUNCATE blocked_ips CASCADE'; " +
                "EXECUTE 'TRUNCATE client_devices CASCADE'; " +
                "EXECUTE 'TRUNCATE countries CASCADE'; " +
                "EXECUTE 'TRUNCATE currencies CASCADE'; " +
                "EXECUTE 'TRUNCATE database_logs CASCADE'; " +
                "EXECUTE 'TRUNCATE jwt_black_lists CASCADE'; " +
                "EXECUTE 'TRUNCATE jwt_token_use_logs CASCADE'; " +
                "EXECUTE 'TRUNCATE login_fail_logs CASCADE'; " +
                "EXECUTE 'TRUNCATE merchant_past_passwords CASCADE'; " +
                "EXECUTE 'TRUNCATE merchants CASCADE'; " +
                "EXECUTE 'TRUNCATE otp_sender_impls CASCADE'; " +
                "EXECUTE 'TRUNCATE otps CASCADE'; " +
                "EXECUTE 'TRUNCATE past_passwords CASCADE'; " +
                "EXECUTE 'TRUNCATE system_block_schedules CASCADE'; " +
                "EXECUTE 'TRUNCATE used_ips_by_users CASCADE'; " +
                "RETURN 1; " +
                "END; " +*/
                "\$BODY\$ " +
                "LANGUAGE PLPGSQL;",
        nativeQuery = true
    )
    fun createTruncateAllExecutionFunction()

    @Query(
        value = "SELECT * FROM execute_truncate_all()",
        nativeQuery = true
    )
    fun executeTruncateAllExecutionFunction()

    @Query(
        value = "SELECT * FROM execute_countries_queries(:queryToExecute)",
        nativeQuery = true
    )
    fun executeFunction(@Param("queryToExecute") queryToExecute: String): List<Country>

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
        value = "CREATE OR REPLACE FUNCTION execute_global_field_queries (query_to_execute IN VARCHAR) RETURNS TABLE (vlu text, description text) " +
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
    fun createQueryExecutionStoredFunctionGlobal()

    @Query(
        value = "SELECT * FROM execute_global_field_queries(:queryToExecute)",
        nativeQuery = true
    )
    fun executeGlobalFunction(@Param("queryToExecute") queryToExecute: String): List<List<Any>>
    fun findByKnownNameAndDataStatus(knownName: String, dataStatus: DataStatus = DataStatus.ACTIVE): List<Country>
    fun findByOfficialNameAndDataStatus(officialName: String, dataStatus: DataStatus = DataStatus.ACTIVE): List<Country>
    fun findByIdAndDataStatus(id: String, dataStatus: DataStatus = DataStatus.ACTIVE): List<Country>
    fun findByIsdCodeAndDataStatus(isdCode: String, dataStatus: DataStatus = DataStatus.ACTIVE): List<Country>

    @Query(
        value = "SELECT countries.id AS id, " +
                "countries.created_date AS createdDate " +
                "FROM countries " +
                "ORDER BY countries.created_date ASC " +
                "LIMIT 1",
        nativeQuery = true
    )
    fun findTop1ByOrderByCreatedDateAsc(): List<FetchMostRecentMinifiedResponse>

    @Query(
        value = "SELECT COUNT(*) " +
                "FROM countries " +
                "WHERE CASE WHEN :considerServiceable " +
                "THEN serviceable = :serviceable ELSE true END " +
                "AND ( " +
                "known_name SIMILAR TO :queryString " +
                "OR official_name SIMILAR TO :queryString " +
                "OR alpha2 SIMILAR TO :queryString " +
                "OR alpha3 SIMILAR TO :queryString " +
                "OR isd_code SIMILAR TO :queryString " +
                "OR METAPHONE(known_name,7) = METAPHONE(:queryString,7) " +
                "OR METAPHONE(official_name,7) = METAPHONE(:queryString,7) " +
                "OR LOWER(known_name) % LOWER(:queryString) " +
                "OR LOWER(official_name) % LOWER(:queryString) " +
                ") " +
                "ORDER BY created_date ASC,id ASC " +
                "LIMIT :limit ",
        nativeQuery = true
    )
    fun findCountWithOutOffsetIdOffsetDateAndLimit(
        @Param("queryString") queryString: String,
        @Param("serviceable") serviceable: Boolean? = null,
        @Param("considerServiceable") considerServiceable: Boolean
    ): Long

    @Query(
        value = "SELECT countries.id as id, " +
                "CAST(countries.known_name AS VARCHAR) as name, " +
                "CAST(countries.official_name AS VARCHAR) as officialName, " +
                "CAST(countries.isd_code AS VARCHAR) as isdCode, " +
                "CAST(countries.alpha2 AS VARCHAR) as alpha2, " +
                "CAST(countries.alpha3 AS VARCHAR) as alpha3, " +
                "countries.created_date as createdDate " +
                "FROM countries " +
                "WHERE CASE WHEN :considerServiceable " +
                "THEN serviceable = :serviceable ELSE true END " +
                "AND ( " +
                "known_name SIMILAR TO :queryString " +
                "OR official_name SIMILAR TO :queryString " +
                "OR alpha2 SIMILAR TO :queryString " +
                "OR alpha3 SIMILAR TO :queryString " +
                "OR isd_code SIMILAR TO :queryString " +
                "OR METAPHONE(known_name,7) = METAPHONE(:queryString,7) " +
                "OR METAPHONE(official_name,7) = METAPHONE(:queryString,7) " +
                "OR LOWER(known_name) % LOWER(:queryString) " +
                "OR LOWER(official_name) % LOWER(:queryString) " +
                ") " +
                "ORDER BY created_date ASC,id ASC " +
                "LIMIT :limit ",
        nativeQuery = true
    )
    fun findAllDataWithOutOffsetIdOffsetDateAndLimit(
        @Param("queryString") queryString: String,
        @Param("serviceable") serviceable: Boolean? = null,
        @Param("considerServiceable") considerServiceable: Boolean
    ): List<CountryListMinifiedResponse>

    @Query(
        value = "SELECT countries.id as id, " +
                "CAST(countries.known_name AS VARCHAR) as name, " +
                "CAST(countries.official_name AS VARCHAR) as officialName, " +
                "CAST(countries.isd_code AS VARCHAR) as isdCode, " +
                "CAST(countries.alpha2 AS VARCHAR) as alpha2, " +
                "CAST(countries.alpha3 AS VARCHAR) as alpha3, " +
                "countries.created_date as createdDate " +
                "FROM countries " +
                "WHERE created_date > :zonedDateTime " +
                "AND CASE WHEN :considerServiceable THEN serviceable = :serviceable ELSE true END " +
                "AND ( " +
                "known_name SIMILAR TO :queryString " +
                "OR official_name SIMILAR TO :queryString " +
                "OR alpha2 SIMILAR TO :queryString " +
                "OR alpha3 SIMILAR TO :queryString " +
                "OR isd_code SIMILAR TO :queryString " +
                "OR METAPHONE(known_name,7) = METAPHONE(:queryString,7) " +
                "OR METAPHONE(official_name,7) = METAPHONE(:queryString,7) " +
                "OR LOWER(known_name) % LOWER(:queryString) " +
                "OR LOWER(official_name) % LOWER(:queryString) " +
                ") " +
                "ORDER BY created_date ASC,id ASC " +
                "LIMIT :limit ",
        nativeQuery = true
    )
    fun findWithOutOffsetId(
        @Param("queryString") queryString: String,
        @Param("zonedDateTime") zonedDateTime: ZonedDateTime,
        @Param("serviceable") serviceable: Boolean? = null,
        @Param("considerServiceable") considerServiceable: Boolean,
        @Param("limit") limit: Int
    ): List<CountryListMinifiedResponse>

    @Query(
        value = "SELECT countries.id as id, " +
                "CAST(countries.known_name as VARCHAR) as name, " +
                "CAST(countries.official_name AS VARCHAR) as officialName, " +
                "CAST(countries.isd_code AS VARCHAR) as isdCode, " +
                "CAST(countries.alpha2 AS VARCHAR) as alpha2, " +
                "CAST(countries.alpha3 AS VARCHAR) as alpha3, " +
                "countries.created_date as createdDate " +
                "FROM countries " +
                "WHERE created_date = :zonedDateTime " +
                "AND id > :offsetId " +
                "AND CASE WHEN :considerServiceable THEN serviceable = :serviceable ELSE true END " +
                "AND ( " +
                "known_name SIMILAR TO :queryString " +
                "OR official_name SIMILAR TO :queryString " +
                "OR alpha2 SIMILAR TO :queryString " +
                "OR alpha3 SIMILAR TO :queryString " +
                "OR isd_code SIMILAR TO :queryString " +
                "OR METAPHONE(known_name,7) = METAPHONE(:queryString,7) " +
                "OR METAPHONE(official_name,7) = METAPHONE(:queryString,7) " +
                "OR LOWER(known_name) % LOWER(:queryString) " +
                "OR LOWER(official_name) % LOWER(:queryString) " +
                ") " +
                "ORDER BY created_date ASC,id ASC " +
                "LIMIT :limit ",
        nativeQuery = true
    )
    fun findWithOffsetId(
        @Param("queryString") queryString: String,
        @Param("zonedDateTime") zonedDateTime: ZonedDateTime,
        @Param("offsetId") offsetId: String,
        @Param("serviceable") serviceable: Boolean? = null,
        @Param("considerServiceable") considerServiceable: Boolean,
        @Param("limit") limit: Int
    ): List<CountryListMinifiedResponse>

    fun findByAlpha2AndDataStatus(
        alpha2Code: String,
        dataStatus: DataStatus = DataStatus.ACTIVE
    ): List<Country>



    @Query(
        value = "SELECT countries.id as id, " +
                "CAST(countries.known_name as VARCHAR) as name, " +
                "CAST(countries.official_name AS VARCHAR) as officialName, " +
                "CAST(countries.isd_code AS VARCHAR) as isdCode, " +
                "CAST(countries.alpha2 AS VARCHAR) as alpha2, " +
                "CAST(countries.alpha3 AS VARCHAR) as alpha3, " +
                "countries.created_date as createdDate " +
                "FROM countries " +
                "WHERE CASE WHEN :considerServiceable THEN serviceable = :serviceable ELSE true END " +
                "AND ( CASE WHEN :queryString = '%' THEN TRUE ELSE " +
                "known_name SIMILAR TO :queryString " +
                "OR official_name SIMILAR TO :queryString " +
                "OR alpha2 SIMILAR TO :queryString " +
                "OR alpha3 SIMILAR TO :queryString " +
                "OR isd_code SIMILAR TO :queryString " +
                "OR METAPHONE(known_name,7) = METAPHONE(:queryString,7) " +
                "OR METAPHONE(official_name,7) = METAPHONE(:queryString,7) " +
                "OR LOWER(known_name) % LOWER(:queryString) " +
                "OR LOWER(official_name) % LOWER(:queryString) " +
                "END ) " +
                "AND (:dataStatus IS NULL OR countries.data_status = :#{#dataStatus.name()} "

        , nativeQuery = true
    )
    fun findCountWithOutOffsetIdAndDate(
        @Param("queryString") queryString: String,
        @Param("serviceable") serviceable: Boolean? = null,
        @Param("considerServiceable") considerServiceable: Boolean,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE
    ): Long


    @Query(
        value = "SELECT countries.id as id, " +
                "CAST(countries.known_name as VARCHAR) as name, " +
                "CAST(countries.official_name AS VARCHAR) as officialName, " +
                "CAST(countries.isd_code AS VARCHAR) as isdCode, " +
                "CAST(countries.alpha2 AS VARCHAR) as alpha2, " +
                "CAST(countries.alpha3 AS VARCHAR) as alpha3, " +
                "countries.created_date as createdDate " +
                "FROM countries " +
                "WHERE CASE WHEN :considerServiceable THEN serviceable = :serviceable ELSE true END " +
                "AND ( CASE WHEN :queryString = '%' THEN TRUE ELSE " +
                "known_name SIMILAR TO :queryString " +
                "OR official_name SIMILAR TO :queryString " +
                "OR alpha2 SIMILAR TO :queryString " +
                "OR alpha3 SIMILAR TO :queryString " +
                "OR isd_code SIMILAR TO :queryString " +
                "OR METAPHONE(known_name,7) = METAPHONE(:queryString,7) " +
                "OR METAPHONE(official_name,7) = METAPHONE(:queryString,7) " +
                "OR LOWER(known_name) % LOWER(:queryString) " +
                "OR LOWER(official_name) % LOWER(:queryString) " +
                "END ) " +
                "AND (:dataStatus IS NULL OR countries.data_status = :#{#dataStatus.name()} "

        , nativeQuery = true
    )
    fun findDataWithOutOffsetIdAndDate(
        @Param("queryString") queryString: String,
        @Param("serviceable") serviceable: Boolean? = null,
        @Param("considerServiceable") considerServiceable: Boolean,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE
    ): List<CountryListMinifiedResponse>


    @Query(
        value = "SELECT countries.id as id, " +
                "CAST(countries.known_name as VARCHAR) as name, " +
                "CAST(countries.official_name AS VARCHAR) as officialName, " +
                "CAST(countries.isd_code AS VARCHAR) as isdCode, " +
                "CAST(countries.alpha2 AS VARCHAR) as alpha2, " +
                "CAST(countries.alpha3 AS VARCHAR) as alpha3, " +
                "countries.created_date as createdDate " +
                "FROM countries " +
                "WHERE countries.created_date > :offsetDate " +
                "AND countries.data_status = :#{#dataStatus.name()} " +
                "AND CASE WHEN :considerServiceable THEN serviceable = :serviceable ELSE true END " +
                "AND ( CASE WHEN :queryString = '%' THEN TRUE ELSE " +
                "known_name SIMILAR TO :queryString " +
                "OR official_name SIMILAR TO :queryString " +
                "OR alpha2 SIMILAR TO :queryString " +
                "OR alpha3 SIMILAR TO :queryString " +
                "OR isd_code SIMILAR TO :queryString " +
                "OR METAPHONE(known_name,7) = METAPHONE(:queryString,7) " +
                "OR METAPHONE(official_name,7) = METAPHONE(:queryString,7) " +
                "OR LOWER(known_name) % LOWER(:queryString) " +
                "OR LOWER(official_name) % LOWER(:queryString) " +
                "END ) " +
                "ORDER BY created_date ASC,id ASC "+
                "LIMIT :limit "
        , nativeQuery = true
    )
    fun findDataWithOutOffsetId(
        @Param("queryString") queryString: String,
        @Param("serviceable") serviceable: Boolean? = null,
        @Param("considerServiceable") considerServiceable: Boolean,
        @Param("offsetDate") offsetDate: ZonedDateTime,
        @Param("limit") limit: Int,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE
    ): List<CountryListMinifiedResponse>


    @Query(
        value = "SELECT countries.id as id, " +
                "CAST(countries.known_name as VARCHAR) as name, " +
                "CAST(countries.official_name AS VARCHAR) as officialName, " +
                "CAST(countries.isd_code AS VARCHAR) as isdCode, " +
                "CAST(countries.alpha2 AS VARCHAR) as alpha2, " +
                "CAST(countries.alpha3 AS VARCHAR) as alpha3, " +
                "countries.created_date as createdDate " +
                "FROM countries " +
                "WHERE countries.id > :offsetId " +
                "AND countries.created_date = :offsetDate " +
                "AND countries.data_status = :#{#dataStatus.name()} " +
                "AND CASE WHEN :considerServiceable THEN serviceable = :serviceable ELSE true END " +
                "AND ( CASE WHEN :queryString = '%' THEN TRUE ELSE " +
                "known_name SIMILAR TO :queryString " +
                "OR official_name SIMILAR TO :queryString " +
                "OR alpha2 SIMILAR TO :queryString " +
                "OR alpha3 SIMILAR TO :queryString " +
                "OR isd_code SIMILAR TO :queryString " +
                "OR METAPHONE(known_name,7) = METAPHONE(:queryString,7) " +
                "OR METAPHONE(official_name,7) = METAPHONE(:queryString,7) " +
                "OR LOWER(known_name) % LOWER(:queryString) " +
                "OR LOWER(official_name) % LOWER(:queryString) " +
                "END ) "+
                "AND countries.data_status = :#{#dataStatus.name()} " +
                "ORDER BY created_date ASC,id ASC " +
                "LIMIT :limit "
        , nativeQuery = true
    )
    fun findDataWithOffsetId(
        @Param("queryString") queryString: String,
        @Param("serviceable") serviceable: Boolean? = null,
        @Param("considerServiceable") considerServiceable: Boolean,
        @Param("offsetDate") offsetDate: ZonedDateTime,
        @Param("limit") limit: Int,
        @Param("offsetId") offsetId: String,
        @Param("dataStatus") dataStatus: DataStatus = DataStatus.ACTIVE
    ): List<CountryListMinifiedResponse>
}