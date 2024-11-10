package com.alphaStore.Core.repoAggregator

import com.alphaStore.Core.contracts.CountryRepoAggregatorContract
import com.alphaStore.Core.entity.Country
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.minifiedresponseimpl.CountryListMinifiedResponseImpl
import com.alphaStore.Core.minifiedresponseimpl.FetchMostRecentMinifiedResponseImpl
import com.alphaStore.Core.model.AggregatorListResponse
import com.alphaStore.Core.model.AggregatorResponse
import com.alphaStore.Core.repo.CountryRepo
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.ArrayList

@Component
class CountryRepoAggregator(
    private val countryRepo: CountryRepo,
    //val redisObjectMasterContract: RedisObjectMasterContract,
    //val redisCacheCleanerMasterContract: RedisCacheCleanerMasterContract,
    //private val dateUtilContract: DateUtilContract
) : CountryRepoAggregatorContract {

    override fun save(entity: Country): Country {
        //redisCacheCleanerMasterContract.clean(Country::class.java.simpleName, idsChanged = arrayListOf(entity.id))
        return countryRepo.save(entity)
    }

    override fun saveAll(entities: List<Country>) {
        /*redisCacheCleanerMasterContract.clean(
            Country::class.java.simpleName,
            idsChanged = entities.map { it.id }.toCollection(ArrayList())
        )*/
        countryRepo.saveAll(entities)
    }

    override fun executeFunction(queryString: String): List<Country> {
        return countryRepo.executeFunction(queryString)
    }

    override fun executeGlobalFunction(queryString: String): List<List<Any>> {
        return countryRepo.executeGlobalFunction(queryString)
    }

    override fun findByAlpha2AndDataStatus(
        alpha2Code: String,

        skipCache: Boolean
    ): AggregatorListResponse<Country> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            alpha2Code,
            "findByAlpha2AndDataStatus"
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apisAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        Country::class.java.simpleName,
                        hash
                    )
                ), databaseAccessLogId
            )
        }*/
        val resultFromDb = countryRepo.findByAlpha2AndDataStatus(alpha2Code, DataStatus.ACTIVE)
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            Country::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findCountWithOutOffsetIdAndDate(
        queryString: String,
        serviceable: Boolean?,
        dataStatus: DataStatus,
        skipCache: Boolean
    ): AggregatorResponse<Long> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            queryString,
            "findCountWithOutOffsetIdAndDate"
        )*/
        /*if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorResponse(
                data =
                redisObjectMasterContract.getDatabaseResultCache(
                    Country::class.java.simpleName,
                    hash
                ).toLong(), databaseAccessLogId
            )
        }*/
        val resultFromDb =
            countryRepo.findCountWithOutOffsetIdAndDate(
                queryString = queryString,
                serviceable = serviceable,
                considerServiceable = serviceable != null
            )
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            Country::class.java,
            hash,
            resultFromDb,
            arrayListOf()
        )*/
        return AggregatorResponse(data = resultFromDb, databaseAccessLogId)
    }

    override fun findDataWithOutOffsetIdAndDate(
        queryString: String,
        serviceable: Boolean?,
        dataStatus: DataStatus,
        skipCache: Boolean
    ): AggregatorListResponse<CountryListMinifiedResponseImpl> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            queryString,
            "findDataWithOutOffsetIdAndDate"
        )*/
        /*if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList<CountryListMinifiedResponseImpl>(
                    redisObjectMasterContract.getDatabaseResultCache(
                        Country::class.java.simpleName,
                        hash
                    )
                ), databaseAccessLogId
            )
        }*/
        val resultFromDb =
            countryRepo.findDataWithOutOffsetIdAndDate(
                queryString = queryString,
                serviceable = serviceable,
                considerServiceable = serviceable != null
            ).map { toMap ->
                CountryListMinifiedResponseImpl(
                    id = toMap.id,
                    name = toMap.name,
                    createdDate = toMap.createdDate,
                    officialName = toMap.officialName,
                    isdCode = toMap.isdCode,
                    alpha2 = toMap.alpha2,
                    alpha3 = toMap.alpha3,
                )
            }
                .toCollection(java.util.ArrayList())
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            Country::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findDataWithOutOffsetId(
        queryString: String,
        serviceable: Boolean?,
        skipCache: Boolean,
        limit: Int,
        offsetDate: ZonedDateTime,
        dataStatus: DataStatus
    ): AggregatorListResponse<CountryListMinifiedResponseImpl> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            queryString,
            offsetDate.toString(),
            limit.toString(),
            "findDataWithOutOffsetId"
        )*/
        /*if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList<CountryListMinifiedResponseImpl>(
                    redisObjectMasterContract.getDatabaseResultCache(
                        Country::class.java.simpleName,
                        hash
                    )
                ), databaseAccessLogId
            )
        }*/
        val resultFromDb =
            countryRepo.findDataWithOutOffsetId(
                queryString = queryString,
                serviceable = serviceable,
                considerServiceable = serviceable != null,
                dataStatus = DataStatus.ACTIVE,
                limit = limit,
                offsetDate = offsetDate
            ).map { toMap ->
                val data = CountryListMinifiedResponseImpl(
                    id = toMap.id,
                    name = toMap.name,
                    createdDate = toMap.createdDate,
                    officialName = toMap.officialName,
                    isdCode = toMap.isdCode,
                    alpha2 = toMap.alpha2,
                    alpha3 = toMap.alpha3
                )
                data
            }.toCollection(java.util.ArrayList())

        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            Country::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(java.util.ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findDataWithOffsetId(
        queryString: String,
        serviceable: Boolean?,
        offsetId: String,
        limit: Int,
        offsetDate: ZonedDateTime,
        skipCache: Boolean
    ): AggregatorListResponse<CountryListMinifiedResponseImpl> {
        val databaseAccessLogId = UUID.randomUUID().toString()

        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            queryString,
            offsetDate.toString(),
            limit.toString(),
            "findDataWithOffsetId"
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList<CountryListMinifiedResponseImpl>(
                    redisObjectMasterContract.getDatabaseResultCache(
                        Country::class.java.simpleName,
                        hash
                    )
                ), databaseAccessLogId
            )
        }*/

        val resultFromDb =
            countryRepo.findDataWithOffsetId(
                queryString = queryString,
                serviceable = serviceable,
                considerServiceable = serviceable!=null,
                limit = limit,
                offsetDate = offsetDate,
                offsetId = offsetId,
                dataStatus = DataStatus.ACTIVE
            ).map { toMap ->
                val data = CountryListMinifiedResponseImpl(
                    id = toMap.id,
                    name = toMap.name,
                    createdDate = toMap.createdDate,
                    officialName = toMap.officialName,
                    isdCode = toMap.isdCode,
                    alpha2 = toMap.alpha2,
                    alpha3 = toMap.alpha3
                )
                data
            }.toCollection(java.util.ArrayList())
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }



    override fun findByKnownNameAndDataStatus(
        knownName: String,
        dataStatus: DataStatus,
        skipCache: Boolean,
    ): AggregatorListResponse<Country> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            knownName,
            dataStatus.name,
            "findByKnownNameAndDataStatus"
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        Country::class.java.simpleName,
                        hash
                    )
                ), databaseAccessLogId
            )
        }*/
        val resultFromDb = countryRepo.findByKnownNameAndDataStatus(knownName, dataStatus)
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            Country::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findByOfficialNameAndDataStatus(
        officialName: String,
        dataStatus: DataStatus,
        skipCache: Boolean
    ): AggregatorListResponse<Country> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            officialName,
            dataStatus.name,
            "findByOfficialNameAndDataStatus"
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        Country::class.java.simpleName,
                        hash
                    )
                ), databaseAccessLogId
            )
        }*/
        val resultFromDb = countryRepo.findByOfficialNameAndDataStatus(officialName, dataStatus)
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            Country::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findByIdAndDataStatus(
        id: String,
        dataStatus: DataStatus,
        skipCache: Boolean
    ): AggregatorListResponse<Country> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            id,
            dataStatus.name,
            "findByIdAndDataStatus"
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        Country::class.java.simpleName,
                        hash
                    )
                ), databaseAccessLogId
            )
        }*/
        val resultFromDb = countryRepo.findByIdAndDataStatus(id, dataStatus)
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            Country::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findByIsdCodeAndDataStatus(
        isdCode: String,
        dataStatus: DataStatus,
        skipCache: Boolean
    ): AggregatorListResponse<Country> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            isdCode,
            dataStatus.name,
            "findByIsdCodeAndDataStatus"
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList(
                    redisObjectMasterContract.getDatabaseResultCache(
                        Country::class.java.simpleName,
                        hash
                    )
                ), databaseAccessLogId
            )
        }*/
        val resultFromDb = countryRepo.findByIsdCodeAndDataStatus(isdCode, dataStatus)
        /* redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
             Country::class.java,
             hash,
             resultFromDb,
             resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
         )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findTop1ByOrderByCreatedDateAsc(
        skipCache: Boolean
    ): AggregatorListResponse<FetchMostRecentMinifiedResponseImpl> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            "findTop1ByOrderByCreatedDateAsc"
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList<FetchMostRecentMinifiedResponseImpl>(
                    redisObjectMasterContract.getDatabaseResultCache(
                        Country::class.java.simpleName,
                        hash
                    )
                ), databaseAccessLogId
            )
        }*/
        val resultFromDb =
            countryRepo.findTop1ByOrderByCreatedDateAsc()
                .map { toMap ->
                    FetchMostRecentMinifiedResponseImpl(
                        id = toMap.id,
                        createdDate = toMap.createdDate
                    )
                }.toCollection(java.util.ArrayList())
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            Country::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findCountWithOutOffsetIdOffsetDateAndLimit(
        queryString: String,
        serviceable: Boolean?,
        skipCache: Boolean
    ): AggregatorResponse<Long> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            queryString,
            serviceable.toString(),
            "findCountWithOutOffsetIdOffsetDateAndLimit",
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorResponse(
                data = redisObjectMasterContract.getDatabaseResultCache(
                    Country::class.java.simpleName,
                    hash
                ).toLong(), databaseAccessLogId
            )
        }*/
        val resultFromDb = countryRepo.findCountWithOutOffsetIdOffsetDateAndLimit(
            queryString = queryString,
            serviceable = serviceable,
            considerServiceable = serviceable != null
        )
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            Country::class.java,
            hash,
            resultFromDb.toString(),
            arrayListOf()
        )*/
        return AggregatorResponse(data = resultFromDb, databaseAccessLogId)
    }

    override fun findAllDataWithOutOffsetIdOffsetDateAndLimit(
        queryString: String,
        serviceable: Boolean?,
        skipCache: Boolean
    ): AggregatorListResponse<CountryListMinifiedResponseImpl> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            queryString,
            serviceable.toString(),
            "findAllDataWithOutOffsetIdOffsetDateAndLimit",
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList<CountryListMinifiedResponseImpl>(
                    redisObjectMasterContract.getDatabaseResultCache(
                        Country::class.java.simpleName,
                        hash
                    )
                ), databaseAccessLogId
            )
        }*/
        val resultFromDb = countryRepo.findAllDataWithOutOffsetIdOffsetDateAndLimit(
            queryString = queryString,
            serviceable = serviceable,
            considerServiceable = serviceable != null
        ).map { toMap ->
            CountryListMinifiedResponseImpl(
                id = toMap.id,
                name = toMap.name,
                officialName = toMap.officialName,
                isdCode = toMap.isdCode,
                alpha2 = toMap.alpha2,
                alpha3 = toMap.alpha3,
                createdDate = toMap.createdDate
            )
        }.toCollection(java.util.ArrayList())
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            Country::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }

    override fun findWithOutOffsetId(
        queryString: String,
        zonedDateTime: ZonedDateTime,
        serviceable: Boolean?,
        limit: Int,
        skipCache: Boolean
    ): AggregatorListResponse<CountryListMinifiedResponseImpl> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            queryString,
            zonedDateTime.toString(),
            serviceable.toString(),
            limit.toString(),
            "findWithOutOffsetId",
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList<CountryListMinifiedResponseImpl>(
                    redisObjectMasterContract.getDatabaseResultCache(
                        Country::class.java.simpleName,
                        hash
                    )
                ), databaseAccessLogId
            )
        }*/
        val resultFromDb = countryRepo.findWithOutOffsetId(
            queryString = queryString,
            zonedDateTime = zonedDateTime,
            serviceable = serviceable,
            considerServiceable = serviceable!=null,
            limit = limit
        ).map { toMap ->
            CountryListMinifiedResponseImpl(
                id = toMap.id,
                name = toMap.name,
                officialName = toMap.officialName,
                isdCode = toMap.isdCode,
                alpha2 = toMap.alpha2,
                alpha3 = toMap.alpha3,
                createdDate = toMap.createdDate
            )
        }.toCollection(java.util.ArrayList())
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            Country::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }



    override fun findWithOffsetId(
        queryString: String,
        offsetDate: ZonedDateTime,
        offsetId: String,
        serviceable: Boolean?,
        limit: Int,
        skipCache: Boolean
    ): AggregatorListResponse<CountryListMinifiedResponseImpl> {
        val databaseAccessLogId = UUID.randomUUID().toString()
        /*val hash = HashMaster.getHash(
            Country::class.java.simpleName,
            queryString,
            zonedDateTime.toString(),
            offsetId.toString(),
            serviceable.toString(),
            limit.toString(),
            "findWithOffsetId",
        )
        if (!skipCache &&
            redisObjectMasterContract.checkIfPresentDatabaseResultCache(
                Country::class.java.simpleName,
                hash
            )
        ) {
            superDatabaseLogMaster.dataFoundAtCache(apiAccessLogId, databaseAccessLogId)
            return AggregatorListResponse(
                data = ConverterStringToObjectList.getObjectList<CountryListMinifiedResponseImpl>(
                    redisObjectMasterContract.getDatabaseResultCache(
                        Country::class.java.simpleName,
                        hash
                    )
                ), databaseAccessLogId
            )
        }*/
        val resultFromDb = countryRepo.findWithOffsetId(
            queryString = queryString,
            zonedDateTime =offsetDate,
            offsetId = offsetId,
            serviceable = serviceable,
            considerServiceable = serviceable!=null,
            limit = limit
        ).map { toMap ->
            CountryListMinifiedResponseImpl(
                id = toMap.id,
                name = toMap.name,
                officialName = toMap.officialName,
                isdCode = toMap.isdCode,
                alpha2 = toMap.alpha2,
                alpha3 = toMap.alpha3,
                createdDate = toMap.createdDate
            )
        }.toCollection(java.util.ArrayList())
        /*redisObjectMasterContract.saveDatabaseResultCacheWithEntityClass(
            Country::class.java,
            hash,
            resultFromDb,
            resultFromDb.map { toMap -> toMap.id }.toCollection(ArrayList())
        )*/
        return AggregatorListResponse(data = ArrayList(resultFromDb), databaseAccessLogId)
    }
}