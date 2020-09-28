package com.generator.demo.dto

data class Address(
    var house: String,
    var street: String,
    var postalCode: String,
    var city: String,
    var county: String,
    var state: String?,
    var stateCode: String?,
    var country: String?,
    var countryCode: String
)
