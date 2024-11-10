package com.alphaStore.Core.model

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class UpdateCountryServiceRequest(
    @field:NotNull(message = "Please provide countryId")
    @field:NotEmpty(message = "Please provide countryId")
    val countryId: String,
    val serviceable: Boolean = false
)