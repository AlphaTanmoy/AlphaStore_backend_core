package com.alphaStore.Core.repoAggregator

import com.alphaStore.Core.contracts.ClientDeviceRepoAggregatorContract
import com.alphaStore.Core.contracts.DateUtilContract
import com.alphaStore.Core.entity.AccessRole
import com.alphaStore.Core.entity.ClientDevice
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.enums.UserType
import com.alphaStore.Core.model.AggregatorListResponse
import com.alphaStore.Core.model.AggregatorResponse
import com.alphaStore.Core.repo.ClientDeviceRepo
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.ArrayList

@Component
class ClientDeviceRepoAggregator(
    private val clientDeviceRepo: ClientDeviceRepo,
    private val dateUtilContract: DateUtilContract,
    private val clientDeviceRepoAggregatorQueryBuilder: ClientDeviceRepoAggregatorQueryBuilder,
) : ClientDeviceRepoAggregatorContract {


    override fun dropTable() {
        //redisCacheCleanerMasterContract.clean(ClientDevice::class.java.simpleName, idsChanged = null)
        clientDeviceRepo.dropTable()
    }

    override fun executeFunction(queryToExecute: String): List<ClientDevice> {
        return clientDeviceRepo.executeFunction(queryToExecute)
    }

    override fun saveAll(clientDevices: java.util.ArrayList<ClientDevice>) {
        /*redisCacheCleanerMasterContract.clean(
            ClientDevice::class.java.simpleName,
            idsChanged = clientDevices.map { it.id }.toCollection(ArrayList())
        )*/
        clientDeviceRepo.saveAll(clientDevices)
    }

    override fun save(clientDevice: ClientDevice) {
//        redisCacheCleanerMasterContract.clean(
//            ClientDevice::class.java.simpleName,
//            idsChanged = arrayListOf(clientDevice.id)
//        )
        clientDeviceRepo.save(clientDevice)
    }

    override fun findClientDevicesWithSameFcmIdsWhichIsNotBeingUsedElseWhere(
        fcmId: String,
        userId: String?,
        accessRole: AccessRole?,
        userType: UserType?,

        skipCache: Boolean
    ): AggregatorListResponse<ClientDevice> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            ClientDevice::class.java.simpleName,
            "findClientDevicesWithSameFcmIdsWhichIsNotBeingUsedElseWhere",
            fcmId,
            userId ?: "",
            accessRole?.id ?: "",
            userType?.name ?: ""
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                ClientDevice::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apisAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        ClientDevice::class.java.simpleName,
                        hash
                    )
                ),
                databaseAccessLogId
            )
        }*/
        val resultFromDb = accessRole?.let { accessRolePositive ->
            val queryString =
                clientDeviceRepoAggregatorQueryBuilder.findClientDevicesWithSameFcmIdsWhichIsNotBeingUsedElseWhereQueryBuilder(
                    fcmId,
                    true,
                    accessRolePositive,
                    userId!!,
                    userType!!,
                )
            clientDeviceRepo.executeFunction(queryString)
        } ?: run {
            clientDeviceRepo.findByFcmIdAndFcmIdBeingUsedElseWhereAndDataStatus(
                fcmId,
                true,
                DataStatus.ACTIVE
            )
        }
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            ClientDevice::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId = databaseAccessLogId)
    }

    override fun findByIdAndDataStatus(
        deviceId: String,
    ): AggregatorListResponse<ClientDevice> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            ClientDevice::class.java.simpleName,
            "findByIdAndDataStatus",
            deviceId
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                ClientDevice::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apisAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        ClientDevice::class.java.simpleName,
                        hash
                    )
                ),
                databaseAccessLogId
            )
        }*/
        val resultFromDb = clientDeviceRepo.findByIdAndDataStatus(id = deviceId, dataStatus = DataStatus.ACTIVE)
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            ClientDevice::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId = databaseAccessLogId)

    }

    override fun deactivateClientDevices(
        negativeSimilarToCondition: String
    ) {
        //redisCacheCleanerMasterContract.clean(ClientDevice::class.java.simpleName, idsChanged = null)
        clientDeviceRepo.deactivateClientDevices(negativeSimilarToCondition)
    }

    override fun getDevicesToMarkAsActive(
        similarToCondition: String,

        skipCache: Boolean,
    ): AggregatorListResponse<ClientDevice> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            ClientDevice::class.java.simpleName,
            "getDevicesToMarkAsActive",
            similarToCondition
        )
        if (
            !skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                ClientDevice::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apisAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        ClientDevice::class.java.simpleName,
                        hash
                    )
                ),
                databaseAccessLogId
            )
        }*/
        val resultFromDb = clientDeviceRepo.getDevicesToMarkAsActive(similarToCondition)
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            ClientDevice::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId = databaseAccessLogId)
    }

    override fun findByDataOrderByCreatedDateAsc(
        userId: String,
        dataStatus: DataStatus,

        skipCache: Boolean
    ): AggregatorListResponse<ClientDevice> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            ClientDevice::class.java.simpleName,
            "findByDataOrderByCreatedDateAsc",
            userId, dataStatus.name
        )*/
        /*if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                ClientDevice::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apisAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        ClientDevice::class.java.simpleName,
                        hash
                    )
                ),
                databaseAccessLogId
            )
        }*/
        val resultFromDb = clientDeviceRepo.findByDataOrderByCreatedDateAsc(
            userId = userId,
            dataStatus = dataStatus
        )
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            ClientDevice::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId = databaseAccessLogId)
    }

    override fun countByData(
        userId: String,
        dataStatus: DataStatus,

        skipCache: Boolean
    ): AggregatorResponse<Long> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /* val hash = HashMaster.getHash(
             ClientDevice::class.java.simpleName,
             "countByData",
             userId, dataStatus.name
         )
         if (!skipCache &&
             redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                 ClientDevice::class.java.simpleName,
                 hash
             )
         ) {
             superDatabaseLogMaster.dataFoundAtCache(apisAccessLogId, databaseAccessLogId)
             return AggregatorResponse(
                 data =
                 redisObjectMasterContract.getDatabaseResultCache(
                     ClientDevice::class.java.simpleName,
                     hash
                 ).toLong(),
                 databaseAccessLogId
             )
         }*/
        val resultFromDb = clientDeviceRepo.countByData(
            userId = userId,
            dataStatus = dataStatus
        )
        /* redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
             ClientDevice::class.java,
             hash,
             resultFromDb,
             arrayListOf()
         )*/
        return AggregatorResponse(data = resultFromDb, databaseAccessLogId = databaseAccessLogId)

    }


    override fun findByDataWithOffsetId(
        userId: String,
        offsetId: String,
        offsetDate: ZonedDateTime,
        limit: Int,
        dataStatus: DataStatus,

        skipCache: Boolean
    ): AggregatorListResponse<ClientDevice> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            ClientDevice::class.java.simpleName,
            "findByDataWithOffsetId",
            userId,
            offsetId,
            dateUtilContract.getStringFromZonedDateTimeUsingIsoDateFormat(offsetDate),
            limit.toString(),
            dataStatus.name
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                ClientDevice::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apisAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        ClientDevice::class.java.simpleName,
                        hash
                    )
                ),
                databaseAccessLogId
            )
        }*/
        val resultFromDb = clientDeviceRepo.findByDataWithOffsetId(
            userId = userId,
            dataStatus = dataStatus,
            offsetDate = offsetDate,
            offsetId = offsetId,
            limit = limit
        )
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            ClientDevice::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId = databaseAccessLogId)
    }


    override fun findByDataWithOutOffsetId(
        userId: String,
        offsetDate: ZonedDateTime,
        limit: Int,
        dataStatus: DataStatus,

        skipCache: Boolean
    ): AggregatorListResponse<ClientDevice> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            ClientDevice::class.java.simpleName,
            "findByDataWithOutOffsetId",
            userId,
            dateUtilContract.getStringFromZonedDateTimeUsingIsoDateFormat(offsetDate),
            limit.toString(),
            dataStatus.name
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                ClientDevice::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apisAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        ClientDevice::class.java.simpleName,
                        hash
                    )
                ),
                databaseAccessLogId
            )
        }*/
        val resultFromDb = clientDeviceRepo.findByDataWithOutOffsetId(
            userId = userId,
            dataStatus = dataStatus,
            offsetDate = offsetDate,
            limit = limit
        )
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            ClientDevice::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId = databaseAccessLogId)
    }

    override fun findByIdAndUserIdAndDataStatus(
        id: String,
        merchantId: String?,
        dataStatus: DataStatus,

        skipCache: Boolean
    ): AggregatorListResponse<ClientDevice> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            ClientDevice::class.java.simpleName,
            "findByIdAndUserIdAndDataStatus",
            id,
            merchantId ?: "",
            dataStatus.name
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                ClientDevice::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apisAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        ClientDevice::class.java.simpleName,
                        hash
                    )
                ),
                databaseAccessLogId
            )
        }*/
        val resultFromDb = clientDeviceRepo.findByIdAndUserIdAndDataStatus(id, merchantId, dataStatus)
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            ClientDevice::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId = databaseAccessLogId)
    }

    override fun findByUniqueIdentifierIdAndAdminIdOrCustomerIdAndDataStatus(
        uniqueIdentifierId: String,
        userId: String,
        accessRole: AccessRole?,
        userType: UserType?,
        dataStatus: DataStatus,

        skipCache: Boolean
    ): AggregatorListResponse<ClientDevice> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            ClientDevice::class.java.simpleName,
            "findByUniqueIdentifierIdAndAdminIdOrCustomerIdAndDataStatus",
            uniqueIdentifierId,
            userId,
            accessRole?.id ?: "",
            userType?.name ?: "",
            dataStatus.name
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                ClientDevice::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apisAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        ClientDevice::class.java.simpleName,
                        hash
                    )
                ),
                databaseAccessLogId
            )
        }*/
        val resultFromDb =
            clientDeviceRepo.findByUniqueIdentifierIdAndAdminIdOrCustomerIdAndDataStatus(
                uniqueIdentifierId,
                userId,
                dataStatus,
                true
            )

//        redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
//            ClientDevice::class.java,
//            hash,
//            resultFromDb,
//            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
//        )
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId = databaseAccessLogId)
    }

    override fun findByUniqueIdentifierIdAndAdminUserIdAndDataStatus(
        uniqueIdentifierId: String,
        userId: String,
        accessRole: AccessRole?,
        userType: UserType?,
        dataStatus: DataStatus,

        skipCache: Boolean,
    ): AggregatorListResponse<ClientDevice> {
        val databaseAccessLogId = UUID.randomUUID().toString()
//        if (!skipCache &&
//            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
//                ClientDevice::class.java.simpleName,
//                hash
//            )
//        ) {
//            superDatabaseLogMaster.dataFoundAtCache(apisAccessLogId, databaseAccessLogId)
//            return AggregatorListResponse(
//                data = ConverterStringToObjectList.getObjectList(
//                    redisObjectMasterContract.getDatabaseResultCache(
//                        ClientDevice::class.java.simpleName,
//                        hash
//                    )
//                ),
//                databaseAccessLogId
//            )
//        }
        val resultFromDb =
            clientDeviceRepo.findByUniqueIdentifierIdAndAdminIdOrCustomerIdAndDataStatus(
                uniqueIdentifierId,
                userId,
                dataStatus,
                true,
            )
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId = databaseAccessLogId)
    }
}