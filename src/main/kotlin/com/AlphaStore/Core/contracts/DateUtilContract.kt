package com.alphaStore.Core.contracts

import java.time.ZonedDateTime
import java.util.*

interface DateUtilContract {
    fun getStringFromZonedDateTimeUsingIsoDateFormat(zonedDateTime: ZonedDateTime? = null): String

    fun getStringFromZonedDateTimeForPostgres(zonedDateTime: ZonedDateTime? = null): String

    fun getStringFromZonedDateTimeForFrontEndFriendly(zonedDateTime: ZonedDateTime? = null): String

    fun getStringFromZonedDateTimeForClearBankRequest(zonedDateTime: ZonedDateTime? = null): String

    fun getUserFriendlyDateTimeFromPostgresDateTime(zonedDateTime: ZonedDateTime? = null): String

    fun getUserFriendlyDateFromPostgresDateTime(zonedDateTime: ZonedDateTime? = null): String

    fun getZonedDateFromString(
        stringRep: String,
        format: String,
        timeZone: String? = null
    ): Optional<ZonedDateTime>

    fun getZonedDateTimeFromString(
        stringRep: String,
        format: String,
        timeZone: String? = null
    ): Optional<ZonedDateTime>

    fun getZonedDateTimeFromStringUsingIsoFormatServerTimeZone(
        stringRep: String,
    ): Optional<ZonedDateTime>

    fun getZonedDateTimeFromStringFromModulrUTCToServerLocal(
        stringRep: String,
    ): Optional<ZonedDateTime>

    fun getZonedDateTimeFromStringFromClearBankUTCToServerLocal(
        stringRep: String,
    ): Optional<ZonedDateTime>

    fun getDateFromZonedDateTime(zonedDateTime: ZonedDateTime): Date

    fun getMinZonedDateTime(): ZonedDateTime

    fun getMaxZonedDateTime(): ZonedDateTime

    fun getDateForSystemBlockScheduleController(dateString: String? = null): ZonedDateTime
}