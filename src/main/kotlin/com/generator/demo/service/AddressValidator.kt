package com.generator.demo.service

import com.generator.demo.dto.Address
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import kotlin.jvm.Throws

class AddressValidationException(message: String) : RuntimeException(message)

@Component
class AddressValidator {

    private val alphaNumericRegexp = "^[a-zA-Z0-9 ]*$"
    private val charactersRegexp = "^[-()_',/a-zA-Zñáéíóúüëšöïğ ]*$"
    private val alphaNumericSpecialCharactersRegexp = "^[-()_',/a-zA-Z0-9ñáéíóúüëšöïğ ]*$"
    private val countryCodeRegexp = "^[A-Z]{3}$"

    internal var errorList: MutableList<String> = mutableListOf()

    /**
     * Validate given address based on regexp rules
     * Throws exception if any or multiple rules violated
     */
    @Throws(AddressValidationException::class)
    fun validate(address: Address) {
        // house String House or street number.
        validate("house", address.house, alphaNumericRegexp)
        // street String Street name (in practice may also contain street number).
        validate("street", address.street, alphaNumericSpecialCharactersRegexp)
        // postalCode String An alphanumeric string included in a postal address to facilitate mail sorting (a.k.a. post code, postcode, or ZIP code).
        validate("postalCode", address.postalCode, alphaNumericSpecialCharactersRegexp)
        // city String The name of the primary locality of the place.
        validate("city", address.city, charactersRegexp)
        // county String A division of a state; typically a secondary-level administrative division of a country or equivalent.
        validate("county", address.county, charactersRegexp)
        // state String; optional A division of a country; typically a first-level administrative division of a country and/or a geographical region.
        validate("state", address.state, charactersRegexp)
        // stateCode String; optional A code/abbreviation for the state division of a country.
        validate("stateCode", address.stateCode, charactersRegexp)
        // country String; optional The localised country name.
        validate("country", address.country, charactersRegexp)
        // countryCode String (ISO 3166-1 alpha-3 code) A three-letter country code.
        validate("countryCode", address.countryCode, countryCodeRegexp)

        if (errorList.isNotEmpty()) {
            throw AddressValidationException(errorList.joinToString { it })
        }
    }

    private fun validate(key: String, value: String?, regexp: String) {
        if (value != null && !regexp.toRegex().containsMatchIn(value)) {
            errorList.add("$key field validation failed. Value - $value")
        }
    }
}
