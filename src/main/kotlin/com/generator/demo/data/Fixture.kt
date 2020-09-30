package com.generator.demo.data

import com.generator.demo.dto.Address

/**
 * Class container for objects with hardcoded data
 * Used as a data provider for data generator and for testing
 */
class Fixture {

    class Address {
        companion object {

            fun unitedStates() = Address(
                house = "07858",
                street = "Cummerata Court",
                postalCode = "07354-5466",
                city = "South Michaele",
                county = "North Bergen",
                state = "South Carolina",
                stateCode = "MT",
                country = "United States Of America",
                countryCode = "USA"
            )

            fun canada() = Address(
                house = "4355",
                street = "Jamal Rue",
                postalCode = "E2E 5C5",
                city = "North Antonville",
                county = "Oklahoma",
                state = "Alberta",
                stateCode = "AB",
                country = "Canada",
                countryCode = "CND"
            )

            fun mexico() = Address(
                house = "476",
                street = "Parque Morelos",
                postalCode = "10745",
                city = "Culiacán",
                county = "Los Alamos",
                state = "Puebla",
                stateCode = "QUE",
                country = "Mexico",
                countryCode = "MEX"
            )

            fun netherlands() = Address(
                house = "652a",
                street = "Alienelaan",
                postalCode = "7936 SW",
                city = "Oost Proostdam",
                county = "Holland",
                state = "Zuid-Holland",
                stateCode = "MS",
                country = "Netherlands",
                countryCode = "NLD"
            )
        }
    }
}
