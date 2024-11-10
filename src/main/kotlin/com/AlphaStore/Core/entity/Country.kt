package com.alphaStore.Core.entity

import com.fasterxml.jackson.annotation.JsonFilter
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany

@Entity(name = "countries")
data class Country(
    @Column(columnDefinition = "citext")
    var knownName: String = "",
    @Column(columnDefinition = "citext")
    var officialName: String = "",
    @Column(columnDefinition = "citext")
    var isdCode: String = "",
    @Column(columnDefinition = "citext")
    var alpha2: String = "",
    @Column(columnDefinition = "citext")
    var alpha3: String = "",
    var serviceable: Boolean = false,
    var mobileNumberValidationRegex: String = "",
    //@OneToMany(mappedBy = "country", fetch = FetchType.EAGER)
    //var otpSenderImpls: List<OtpSenderImpl> = ArrayList(),
) : SuperEntityWithIdCreatedLastModifiedDataStatus()

@JsonFilter("CountryFilter")
class CountryMixIn