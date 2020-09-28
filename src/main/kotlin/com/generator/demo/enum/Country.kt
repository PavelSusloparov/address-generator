package com.generator.demo.enum

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.Locale

enum class Country(
    @JsonValue
    val countryAbbr: String,
    @JsonValue
    val locale: Locale
) {
    US("US", Locale.US),
    CANADA("CN", Locale.CANADA),
    MEXICO("MX", Locale("es-MX")),
    NETHERLANDS("NL", Locale("nl-NL"));

    companion object {
        // store an internal map for the enums in memory
        private val enums = values().associateBy { it.countryAbbr }

        // Retrieves the enum from the type, null if no matching enum was found
        @JsonCreator
        @JvmStatic
        fun fromName(countryAbbr: String?): Country? {
            return if (countryAbbr != null) {
                enums[countryAbbr]
            } else {
                null
            }
        }

        @JsonCreator
        @JvmStatic
        fun abbreviations(): List<String> {
            return values().map { it.countryAbbr }
        }
    }
}
