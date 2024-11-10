package com.alphaStore.Core.service

import com.alphaStore.Core.contracts.CountryRepoAggregatorContract
import com.alphaStore.Core.contracts.DateUtilContract
import com.alphaStore.Core.contracts.EncodingUtilContract
import com.alphaStore.Core.contracts.EncryptionMasterContract
import com.alphaStore.Core.entity.Country
import com.alphaStore.Core.enums.DataStatus
import com.alphaStore.Core.enums.DateRangeType
import com.alphaStore.Core.minifiedresponseimpl.CountryListMinifiedResponseImpl
import com.alphaStore.Core.model.FilterOption
import com.alphaStore.Core.model.PaginationResponse
import com.alphaStore.Utils.ConverterStringToObjectList
import com.alphaStore.Utils.contracts.BadRequestException
import org.springframework.stereotype.Component
import java.time.ZoneId
import java.time.ZonedDateTime

@Component
class CountryService(
    private val countryRepoAggregatorContract: CountryRepoAggregatorContract,
    private val encryptionMasterContract: EncryptionMasterContract,
    private val encodingUtilContract: EncodingUtilContract,
    private val dateUtilContract: DateUtilContract
) {


    fun getCountries(
        offsetToken: String?,
        toRetFilterOption: ArrayList<FilterOption>,
        giveCount: Boolean,
        queryStringFinal: String,
        serviceable: Boolean?,
        giveData: Boolean,
        considerMaxDateRange: Boolean,
        dateRangeType: String?,
        pageSizeFinal: Int,
    ): PaginationResponse<CountryListMinifiedResponseImpl> {
        var offsetDateFinal: ZonedDateTime? = null
        var offsetId = ""
        offsetToken?.let {
            val decrypted = encryptionMasterContract.decrypt(
                encodingUtilContract.decode(
                    it
                )
            )
            val splits = decrypted.split("::")
            val decryptedOffsetDate = dateUtilContract.getZonedDateTimeFromStringUsingIsoFormatServerTimeZone(splits[0])
            if (decryptedOffsetDate.isEmpty)
                throw BadRequestException("Please provide valid offset token")
            offsetDateFinal = decryptedOffsetDate.get()
            offsetId = splits[1]
        } ?: run {
            val firstCreated =
                countryRepoAggregatorContract.findTop1ByOrderByCreatedDateAsc()
            offsetDateFinal = if (firstCreated.data.isEmpty())
                null
            else {
                val instant = firstCreated.data[0]
                instant.let {
                    ZonedDateTime.ofInstant(it.createdDate.minusNanos(1000), ZoneId.of("UTC"))
                }
            }
        }
        offsetDateFinal ?: run {
            return PaginationResponse(
                arrayListOf(),
                filterUsed = toRetFilterOption,
            )
        }
        val countriesToReturn: ArrayList<CountryListMinifiedResponseImpl> = ArrayList()
        var dataCount = 0L
        if (giveCount) {
            val countFromDb =
                countryRepoAggregatorContract.findCountWithOutOffsetIdOffsetDateAndLimit(
                    queryString = queryStringFinal,
                    serviceable = serviceable,
                )
            dataCount = countFromDb.data
        }
        if (giveData) {
            if (considerMaxDateRange && dateRangeType != null && dateRangeType == DateRangeType.MAX.name) {
                val allData = countryRepoAggregatorContract.findAllDataWithOutOffsetIdOffsetDateAndLimit(
                    queryString = queryStringFinal,
                    serviceable = serviceable,
                )
                countriesToReturn.addAll(allData.data)
            } else {
                if (offsetId.isEmpty()) {
                    val countriesResultForFirstPage = countryRepoAggregatorContract.findWithOutOffsetId(
                        queryString = queryStringFinal,
                        zonedDateTime = offsetDateFinal!!,
                        serviceable = serviceable,
                        limit = pageSizeFinal,
                    )
                    if (countriesResultForFirstPage.data.isEmpty()) {
                        return PaginationResponse(
                            arrayListOf(),
                            filterUsed = toRetFilterOption,
                        )
                    }
                    countriesToReturn.addAll(countriesResultForFirstPage.data)
                } else {
                    val countriesResultForNextPageWithSameDate = countryRepoAggregatorContract.findWithOffsetId(
                        queryString = queryStringFinal,
                        offsetDate = offsetDateFinal!!,
                        offsetId = offsetId,
                        serviceable = serviceable,
                        limit = pageSizeFinal,
                    )
                    val nextPageSize = pageSizeFinal - countriesResultForNextPageWithSameDate.data.size
                    val countriesResultForNextPage = countryRepoAggregatorContract.findWithOutOffsetId(
                        queryString = queryStringFinal,
                        zonedDateTime = offsetDateFinal!!,
                        serviceable = serviceable,
                        limit = nextPageSize,
                    )
                    countriesToReturn.addAll(countriesResultForNextPageWithSameDate.data)
                    countriesToReturn.addAll(countriesResultForNextPage.data)
                }
            }
            if (countriesToReturn.isEmpty()) {
                return PaginationResponse(
                    arrayListOf(),
                    filterUsed = toRetFilterOption,
                )
            } else {
                return PaginationResponse(
                    ConverterStringToObjectList.sanitizeForOutput(ArrayList(countriesToReturn)),
                    encodingUtilContract.encode(
                        encryptionMasterContract.encrypt(
                            "${
                                countriesToReturn.last().createdDate
                            }::${countriesToReturn.last().id}"
                        )
                    ),
                    filterUsed = toRetFilterOption,
                    recordCount = dataCount.toInt()
                )
            }
        } else {
            return PaginationResponse(
                filterUsed = toRetFilterOption,
                recordCount = dataCount.toInt()
            )
        }
    }

    fun getCountryById(
        countryId: String,
    ): Country {

        val countries = countryRepoAggregatorContract.findByIdAndDataStatus(
            countryId,
            skipCache = true
        )
        if (countries.data.isEmpty()) {
            throw BadRequestException("Country is not found")
        }
        val country = countries.data[0]
        return ConverterStringToObjectList.sanitizeForOutputSingleObject(countries.data[0])
    }

    fun getAllCountry(
        offsetToken: String?,
        toRetFilterOption: ArrayList<FilterOption>,
        giveCount: Boolean,
        queryStringFinal: String,
        serviceable: Boolean?,
        giveData: Boolean,
        dateRangeType: String?,
        limit: Int
    ): PaginationResponse<CountryListMinifiedResponseImpl> {
        var offsetDateFinal: ZonedDateTime? = null
        var offsetId = ""
        offsetToken?.let { offsetTokenPositive ->
            val decrypted = encryptionMasterContract.decrypt(
                encodingUtilContract.decode(
                    offsetTokenPositive
                )
            )
            val splits = decrypted.split("::")
            val decryptedOffsetDate =
                dateUtilContract.getZonedDateTimeFromStringUsingIsoFormatServerTimeZone(splits[0])
            if (decryptedOffsetDate.isEmpty)
                throw BadRequestException("Please provide valid offset token")
            offsetDateFinal = decryptedOffsetDate.get()
            offsetId = splits[1]
        } ?: run {
            val firstCreated =
                countryRepoAggregatorContract.findTop1ByOrderByCreatedDateAsc()

            offsetDateFinal = if (firstCreated.data.isEmpty())
                null
            else {
                val instant = firstCreated.data[0]
                instant.let {
                    ZonedDateTime.ofInstant(it.createdDate.minusNanos(1000), ZoneId.of("UTC"))
                }
            }
        }
        offsetDateFinal ?: run {
            return PaginationResponse(
                arrayListOf(),
                filterUsed = toRetFilterOption,
            )
        }
        val toReturnAllCountries: ArrayList<CountryListMinifiedResponseImpl> = ArrayList()
        var giveCountData = 0L
        if (giveCount) {
            val allCountryCount =
                countryRepoAggregatorContract.findCountWithOutOffsetIdAndDate(
                    queryStringFinal,
                    serviceable,
                )
            giveCountData = allCountryCount.data

        }
        if (giveData) {
            if (dateRangeType != null && dateRangeType == DateRangeType.MAX.name) {
                val allCountry = countryRepoAggregatorContract.findDataWithOutOffsetIdAndDate(
                    queryString = queryStringFinal,
                    serviceable = serviceable
                )


                toReturnAllCountries.addAll(allCountry.data)
            } else {
                if (offsetId == "") {
                    val countryFirstPage =
                        offsetDateFinal?.let {
                            countryRepoAggregatorContract.findDataWithOutOffsetId(
                                serviceable = serviceable,
                                queryString = queryStringFinal,
                                limit = limit,
                                offsetDate = offsetDateFinal!!,
                                dataStatus = DataStatus.ACTIVE
                            )
                        }
                    if (countryFirstPage!!.data.isEmpty())
                        return PaginationResponse(
                            arrayListOf(),
                            recordCount = giveCountData.toInt(),
                            filterUsed = toRetFilterOption
                        )
                    toReturnAllCountries.addAll(countryFirstPage.data)

                } else {
                    val countryNextPageWithSameData =
                        countryRepoAggregatorContract.findDataWithOffsetId(
                            queryString = queryStringFinal,
                            serviceable = serviceable,
                            offsetId = offsetId,
                            limit = limit,
                            offsetDate = offsetDateFinal!!
                        )

                    val nextPageSize = limit - countryNextPageWithSameData.data.size
                    val countryNextPage = countryRepoAggregatorContract.findDataWithOutOffsetId(
                        queryString = queryStringFinal,
                        serviceable = serviceable,
                        limit = nextPageSize,
                        offsetDate = offsetDateFinal!!,
                        dataStatus = DataStatus.ACTIVE
                    )
                    toReturnAllCountries.addAll(countryNextPageWithSameData.data)
                    toReturnAllCountries.addAll(countryNextPage.data)

                }
            }
            if (toReturnAllCountries.isEmpty()) {
                return PaginationResponse(
                    arrayListOf(),
                    recordCount = giveCountData.toInt(),
                    filterUsed = toRetFilterOption
                )
            } else {
                return PaginationResponse(
                    data = ConverterStringToObjectList.sanitizeForOutput<CountryListMinifiedResponseImpl>(
                        toReturnAllCountries
                    ),
                    filterUsed = toRetFilterOption,
                    offsetToken = encodingUtilContract.encode(
                        encryptionMasterContract.encrypt(
                            "${
                                toReturnAllCountries.last().createdDate
                            }::${
                                toReturnAllCountries.last().id
                            }"
                        )
                    ),
                    recordCount = giveCountData.toInt()
                )
            }
        }else {
            return PaginationResponse(
                filterUsed = toRetFilterOption,
                recordCount = giveCountData.toInt(),
                data = toReturnAllCountries
            )
        }

    }



    fun getCountryByIsdCode(
        countryIsdCode: String,
    ): Country {

        val countries = countryRepoAggregatorContract.findByIsdCodeAndDataStatus(
            countryIsdCode,

            )
        if (countries.data.isEmpty()) {
            throw BadRequestException("Country is not found")
        }
        val country = countries.data[0]
        return ConverterStringToObjectList.sanitizeForOutputSingleObject(countries.data[0])
    }

    fun updateCountry(
        country: Country,
        valueBeforeChange: Any,
        valueAfterChange: Any,
        fieldValue: String,
    ) {

        val data = country::class.java.getDeclaredField(fieldValue)
        if (data.trySetAccessible()) {
            data.set(country, valueAfterChange)
            countryRepoAggregatorContract.saveAll(arrayListOf(country))
        }
    }


    /*fun addOtpSender(
        country: Country,
        otpSenderImplRequest: OtpSenderImplRequest,
    ) {

        val activeOtpSenderImpl = otpSenderImplRepoAggregatorContract.getActiveOtpSenderImpl(
            otpSenderImplRequest.name,
            country.id,
        )
        if (activeOtpSenderImpl.data.isNotEmpty())
            throw BadRequestException("Otp Sender implementation already exist.")
        val otpSenderImpl = OtpSenderImpl(
            name = otpSenderImplRequest.name,
            country = country
        )
        val savedOtpSender = otpSenderImplRepoAggregatorContract.save(otpSenderImpl)
    }*/

    fun getCountryByAlpha2(alpha2Code: String): Country? {

        val countryResponse = countryRepoAggregatorContract.findByAlpha2AndDataStatus(
            alpha2Code,
        )
        if (countryResponse.data.isEmpty()) {
            return null
        }
        return countryResponse.data[0]
    }
}