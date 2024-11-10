package com.alphaStore.Core.repoAggregator

import com.alphaStore.Core.contracts.AccessRoleRepoAggregatorContract
import com.alphaStore.Core.entity.AccessRole
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.enums.UserType
import com.alphaStore.Core.minifiedresponseimpl.AccessRoleMinifiedResponseImpl
import com.alphaStore.Core.minifiedresponseimpl.FetchMostRecentMinifiedResponseImpl
import com.alphaStore.Core.model.AggregatorListResponse
import com.alphaStore.Core.model.AggregatorResponse
import com.alphaStore.Core.querybuilder.AccessRoleRepoAggregatorQueryBuilder
import com.alphaStore.Core.repo.AccessRoleRepo
import com.alphaStore.Utils.HashMaster
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.ArrayList

@Component
class AccessRoleRepoAggregator(
    private val accessRoleRepo: AccessRoleRepo,
    private val accessRoleRepoAggregatorQueryBuilder: AccessRoleRepoAggregatorQueryBuilder
) : AccessRoleRepoAggregatorContract {

    override fun save(entity: AccessRole): AccessRole {
        //redisCacheCleanerMasterContract.clean(AccessRole::class.java.simpleName, idsChanged = arrayListOf(entity.id))
        return accessRoleRepo.save(entity)
    }

    override fun executeFunction(queryToExecute: String): List<AccessRole> {
        return accessRoleRepo.executeFunction(queryToExecute)
    }

    override fun saveAll(entities: List<AccessRole>) {
//        redisCacheCleanerMasterContract.clean(
//            AccessRole::class.java.simpleName,
//            idsChanged = entities.map { it.id }.toCollection(ArrayList())
//        )
        accessRoleRepo.saveAll(entities)
    }

    override fun dropTable() {
        //redisCacheCleanerMasterContract.clean(AccessRole::class.java.simpleName, idsChanged = null)
        accessRoleRepo.dropTable()
    }

    override fun findByIdAndDataStatus(
        id: String,
        dataStatus: DataStatus,
        skipCache: Boolean,
    ): AggregatorListResponse<AccessRole> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val hash = HashMaster.getHash(
            AccessRole::class.java.simpleName,
            id,
            dataStatus.name,
            "findByIdAndDataStatus"
        )
//        if (!skipCache &&
//            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
//                AccessRole::class.java.simpleName,
//                hash
//            )
//        ) {
//            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
//            return AggregatorListResponse(
//                ConverterStringToObjectList.getObjectList(
//                    redisObjectMasterContract.getDatabaseResultCache(
//                        AccessRole::class.java.simpleName,
//                        hash
//                    ),
//                ),
//                databaseAccessLogId
//            )
//        }
        val resultFromDb = accessRoleRepo.findByIdAndDataStatus(id, dataStatus)
//        redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
//            AccessRole::class.java,
//            hash,
//            resultFromDb,
//            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
//        )
        return AggregatorListResponse(
            ArrayList(resultFromDb),
            databaseAccessLogId
        )
    }

    override fun findByCodeAndDataStatus(
        code: String,
        accessRole: AccessRole?,
        userId: String?,
        userType: UserType?,
        dataStatus: DataStatus,
        skipCache: Boolean,
    ): AggregatorListResponse<AccessRole> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val hash = HashMaster.getHash(
            AccessRole::class.java.simpleName,
            accessRole?.id ?: "",
            userId ?: "",
            userType?.name ?: "",
            code,
            dataStatus.name,
            "findByCodeAndDataStatus"
        )
        /*if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                AccessRole::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        AccessRole::class.java.simpleName,
                        hash
                    ),
                ),
                databaseAccessLogId
            )
        }*/
        val resultFromDb = accessRole?.let { accessRolePositive ->
            val queryString = accessRoleRepoAggregatorQueryBuilder.findByCodeAndDataStatusQueryBuilder(
                code, accessRolePositive, userId!!, userType!!, dataStatus
            )
            accessRoleRepo.executeFunction(queryString)
        } ?: run {
            accessRoleRepo.findByCodeAndDataStatus(code, dataStatus)
        }
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            AccessRole::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(
            ArrayList(resultFromDb),
            databaseAccessLogId
        )
    }

    override fun countByDataStatus(
        dataStatus: DataStatus,
        skipCache: Boolean,
    ): AggregatorResponse<Long> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val hash = HashMaster.getHash(
            AccessRole::class.java.simpleName,
            dataStatus.name,
            "countByDataStatus"
        )
        /*if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                AccessRole::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorResponse(
                redisObjectMasterContract.getDatabaseResultCache(
                    AccessRole::class.java.simpleName,
                    hash
                ).toLong(),
                databaseAccessLogId
            )
        }*/
        val resultFromDb = accessRoleRepo.countByDataStatus(dataStatus)
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            AccessRole::class.java,
            hash,
            resultFromDb,
            arrayListOf()
        )*/
        return AggregatorResponse(
            resultFromDb,
            databaseAccessLogId
        )
    }

    override fun findByDataStatus(
        dataStatus: DataStatus,
        skipCache: Boolean,
    ): AggregatorListResponse<AccessRole> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val hash = HashMaster.getHash(
            AccessRole::class.java.simpleName,
            dataStatus.name,
            "findByDataStatus"
        )
        /*if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                AccessRole::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        AccessRole::class.java.simpleName,
                        hash
                    )
                ),
                databaseAccessLogId
            )
        }*/
        val resultFromDb = accessRoleRepo.findByDataStatus(dataStatus)
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            AccessRole::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(
            ArrayList(resultFromDb),
            databaseAccessLogId
        )
    }


    override fun getAccessRoleWithTreeCode(
        treeCode: String,
        skipCache: Boolean,
    ): AggregatorListResponse<AccessRole> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val hash = HashMaster.getHash(
            AccessRole::class.java.simpleName,
            treeCode,
            "getAccessRoleWithTreeCode"
        )
        /*if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                AccessRole::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        AccessRole::class.java.simpleName,
                        hash
                    )
                ),
                databaseAccessLogId
            )
        }*/
        val resultFromDb = accessRoleRepo.getAccessRoleWithTreeCode(treeCode)
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            AccessRole::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(
            ArrayList(resultFromDb),
            databaseAccessLogId
        )
    }


    override fun getAllChildrenAccessRoles(
        tweeCodeCondition: String,
        skipCache: Boolean,
    ): AggregatorListResponse<AccessRole> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val hash = HashMaster.getHash(
            AccessRole::class.java.simpleName,
            tweeCodeCondition,
            "getAllChildrenAccessRoles"
        )
        /*if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                AccessRole::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        AccessRole::class.java.simpleName,
                        hash
                    )
                ),
                databaseAccessLogId
            )
        }*/
        val resultFromDb = accessRoleRepo.getAllChildrenAccessRoles(tweeCodeCondition)
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            AccessRole::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(
            ArrayList(resultFromDb),
            databaseAccessLogId
        )
    }

    override fun getHorizontalSiblingsCount(
        treeCodePatternToCheck: String,
        currentLevel: Int,
        skipCache: Boolean,
    ): AggregatorResponse<Long> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        val hash = HashMaster.getHash(
            AccessRole::class.java.simpleName,
            treeCodePatternToCheck,
            currentLevel.toString(),
            "getHorizontalSiblingsCount"
        )
        /*if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                AccessRole::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorResponse(
                redisObjectMasterContract.getDatabaseResultCache(
                    AccessRole::class.java.simpleName,
                    hash
                ).toLong(), databaseAccessLogId
            )
        }*/
        val resultFromDb = accessRoleRepo.getHorizontalSiblingsCount(treeCodePatternToCheck, currentLevel)
        val resultToSave = if (resultFromDb.isEmpty) 0 else resultFromDb.get()
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            AccessRole::class.java,
            hash,
            resultToSave,
            arrayListOf()
        )*/
        return AggregatorResponse(resultToSave, databaseAccessLogId)
    }


    override fun findTop1ByOrderByCreatedDateAsc(skipCache: Boolean): AggregatorListResponse<FetchMostRecentMinifiedResponseImpl> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            AccessRole::class.java.simpleName,
            "findTop1ByOrderByCreatedDateAsc"
        )*/
        val resultFromDb =
            accessRoleRepo.findTop1ByOrderByCreatedDateAsc()
                .map { toMap ->
                    FetchMostRecentMinifiedResponseImpl(
                        id = toMap.id,
                        createdDate = toMap.createdDate.toInstant()
                    )
                }.toCollection(java.util.ArrayList())
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            AccessRole::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(java.util.ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findCountWithOutOffsetIdOffsetDateAndLimit(
        userId: String,
        queryString: String,
        skipCache: Boolean
    ): AggregatorResponse<Long> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            AccessRole::class.java.simpleName,
            "findCountWithOutOffsetIdOffsetDateAndLimit"
        )*/
        val resultFromDb = accessRoleRepo.findCountWithOutOffsetIdOffsetDateAndLimit(
            userId = userId,
            queryString = queryString,
        )
        /* redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
             AccessRole::class.java,
             hash,
             resultFromDb.toString(),
             arrayListOf()
         )*/
        return AggregatorResponse(data = resultFromDb, databaseAccessLogId)
    }

    override fun findAllDataWithOutOffsetIdOffsetDateAndLimit(
        userId: String,
        queryString: String,
        skipCache: Boolean
    ): AggregatorListResponse<AccessRoleMinifiedResponseImpl> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            AccessRole::class.java.simpleName,
            queryString,
            "findAllDataWithOutOffsetIdOffsetDateAndLimit",
        )*/
        val resultFromDb = accessRoleRepo.findAllDataWithOutOffsetIdOffsetDateAndLimit(
            userId = userId,
            queryString = queryString
        ).map { toMap ->
            AccessRoleMinifiedResponseImpl(
                id = toMap.id,
                code = toMap.code,
                description = toMap.description,
                title = toMap.title,
                createdDate = toMap.createdDate
            )
        }.toCollection(java.util.ArrayList())
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            AccessRole::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findWithOutOffsetId(
        userId: String,
        queryString: String,
        zonedDateTime: ZonedDateTime,
        limit: Int,
        skipCache: Boolean
    ): AggregatorListResponse<AccessRoleMinifiedResponseImpl> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            AccessRole::class.java.simpleName,
            queryString,
            "findWithOutOffsetId",
        )*/
        val resultFromDb = accessRoleRepo.findWithOutOffsetId(
            userId = userId,
            queryString = queryString,
            limit = limit,
            offsetDate = zonedDateTime
        ).map { toMap ->
            AccessRoleMinifiedResponseImpl(
                id = toMap.id,
                code = toMap.code,
                description = toMap.description,
                title = toMap.title,
                createdDate = toMap.createdDate
            )
        }.toCollection(java.util.ArrayList())
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            AccessRole::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findWithOffsetId(
        userId: String,
        queryString: String,
        offsetDate: ZonedDateTime,
        offsetId: String,
        limit: Int,
        skipCache: Boolean
    ): AggregatorListResponse<AccessRoleMinifiedResponseImpl> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            AccessRole::class.java.simpleName,
            queryString,
            "findWithOutOffsetId",
        )*/
        val resultFromDb = accessRoleRepo.findWithOffsetId(
            userId = userId,
            queryString = queryString,
            limit = limit,
            offsetId = offsetId,
            offsetDate = offsetDate
        ).map { toMap ->
            AccessRoleMinifiedResponseImpl(
                id = toMap.id,
                code = toMap.code,
                description = toMap.description,
                title = toMap.title,
                createdDate = toMap.createdDate
            )
        }.toCollection(java.util.ArrayList())
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            AccessRole::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

}