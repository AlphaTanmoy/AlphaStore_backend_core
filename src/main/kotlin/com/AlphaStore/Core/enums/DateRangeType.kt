package com.alphaStore.Core.enums

enum class DateRangeType(val nameDescriptor: String, val minusDays: Int, val minusMonth: Int, val minusYear: Int) {
    PAST_ONE_DAY("One Day", 1, 0, 0),
    PAST_ONE_WEEK("One Week", 7, 0, 0),
    PAST_15_DAYS("15 Days", 15, 0, 0),
    PAST_MONTH("One Month", 0, 1, 0),
    PAST_QUARTER("One Quarter", 0, 3, 0),
    PAST_YEAR("One Year", 0, 0, 1),
    MAX("Maximum", 0, 0, 0),
}