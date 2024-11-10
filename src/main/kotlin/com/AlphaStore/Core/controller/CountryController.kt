package com.alphaStore.Core.controller

import com.alphaStore.Core.entity.Country
import com.alphaStore.Core.minifiedresponseimpl.CountryListMinifiedResponseImpl
import com.alphaStore.Core.model.FilterOption
import com.alphaStore.Core.model.PaginationResponse
import com.alphaStore.Core.model.UpdateCountryServiceRequest
import com.alphaStore.Core.service.CountryService
import com.alphaStore.Utils.KeywordsAndConstants
import com.alphaStore.Utils.validation.ValidateForUUID
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.net.URLDecoder


@RestController
@RequestMapping("/countries")
class CountryController(
    private val countryService: CountryService
) {

    @GetMapping(produces = ["application/json"])
    fun getAllCountry(
        @RequestParam queryString: String? = null,
        @RequestParam limit: Int? = null,
        @RequestParam("giveCount") giveCount: Boolean = false,
        @RequestParam("giveData", defaultValue = "true") giveData: Boolean = true,
        @RequestParam("dateRangeType") dateRangeType: String? = null,
        @RequestParam offsetToken: String? = null,
        @RequestParam serviceable: Boolean? = null,
    ): PaginationResponse<CountryListMinifiedResponseImpl> {
        val toRetFilterOption: ArrayList<FilterOption> = ArrayList()

        var queryStringFinal = "%"
        queryString?.let { obj ->
            toRetFilterOption.add(FilterOption("queryString", obj, obj))
            queryStringFinal =
                obj.split(',').joinToString("|") {"%${URLDecoder.decode(it, "UTF-8")}%"}
        }

        serviceable?.let{
            toRetFilterOption.add(
                FilterOption("serviceable",
                    serviceable.toString(),
                    serviceable.toString()
                )
            )
        }

        var pageSizeFinal = KeywordsAndConstants.DEFAULT_PAGE_SIZE

        limit?.let {
            pageSizeFinal = if (it > KeywordsAndConstants.MAX_PAGE_SIZE) KeywordsAndConstants.MAX_PAGE_SIZE else it
        }

        return countryService.getAllCountry(
            offsetToken,
            toRetFilterOption,
            giveCount,
            queryStringFinal,
            serviceable,
            giveData,
            dateRangeType,
            pageSizeFinal
        )
    }

    @GetMapping("/{countryId}")
    fun getCountryById(
        @Valid
        @PathVariable("countryId") countryId: String,
    ): Country {
        ValidateForUUID.check(countryId)
        return countryService.getCountryById(countryId)
    }

    @PutMapping("/updateCountry")
    fun updateCountry(
        @Valid
        @RequestBody updateCountryServiceRequest: UpdateCountryServiceRequest
    ): Country {
        ValidateForUUID.check(updateCountryServiceRequest.countryId, "country")
        val country = countryService.getCountryById(updateCountryServiceRequest.countryId)
        /*if (country.otpSenderImpls.isEmpty())
            throw BadRequestException("Please add at least one Otp sender implementation for country before making it serviceable")*/
        if (country.serviceable != updateCountryServiceRequest.serviceable) {
            countryService.updateCountry(
                country,
                country.serviceable,
                updateCountryServiceRequest.serviceable,
                "serviceable",
            )
            return countryService.getCountryById(country.id)
        }
        return country
    }

    /*@PostMapping("/addOtpSenderImpl")
    fun addOtpSenderImpl(
        @Valid
        @RequestBody otpSenderImplRequest: OtpSenderImplRequest,
    ): Country {
        ValidateForUUID.check(otpSenderImplRequest.countryId)
        KeywordsAndConstants.otpSenderImplementedCSV.split(",").find {
            it == otpSenderImplRequest.name
        } ?: run {
            throw BadRequestException("Otp Sender implementation not found.")
        }
        val country =
            countryService.getCountryById(
                otpSenderImplRequest.countryId
            )
        countryService.addOtpSender(
            country,
            otpSenderImplRequest,
        )
        return countryService.getCountryById(country.id)
    }*/
}