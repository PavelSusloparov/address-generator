package com.generator.demo.service

import com.generator.demo.data.Fixture
import com.generator.demo.dto.Address
import com.generator.demo.enum.Country
import com.github.javafaker.Faker
import com.neovisionaries.i18n.CountryCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import kotlin.jvm.Throws

class IncorrectCountryCodeException(message: String) : RuntimeException(message)

@Service
class AddressRandomizerService(
    private val addressValidator: AddressValidator
) {

    val logger: Logger = LoggerFactory.getLogger(AddressRandomizerService::class.java)

    private final val defaultAddress: MutableMap<Country, Address> = generateDefaultAddresses()

    /**
     * Generate default addresses
     * Used for additional to javaFaker data and default behavior
     * if javaFaker does not pass validation
     */
    private fun generateDefaultAddresses(): MutableMap<Country, Address> {
        val defaultAddress = mutableMapOf<Country, Address>()
        defaultAddress[Country.US] = Fixture.Address.unitedStates()
        defaultAddress[Country.CANADA] = Fixture.Address.canada()
        defaultAddress[Country.MEXICO] = Fixture.Address.mexico()
        defaultAddress[Country.NETHERLANDS] = Fixture.Address.netherlands()
        return defaultAddress
    }

    /**
     * Generate address based on countryAbbr or a random country from a supported list with javaFaker
     * fallback to default address, if javaFaker result does not satisfy requirements
     */
    @Throws(IncorrectCountryCodeException::class)
    fun generateAddress(countryAbbr: String?): Address {
        val country = getCountry(countryAbbr)
        val faker = Faker(country.locale)
        val fakerAddress = faker.address()
        val address = Address(
            house = fakerAddress.buildingNumber(),
            street = fakerAddress.streetName(),
            postalCode = fakerAddress.zipCode(),
            city = fakerAddress.city(),
            // county is not a part of the javaFaker source, use default
            county = defaultAddress[country]?.county ?: faker.lorem().word(),
            state = fakerAddress.state(),
            stateCode = fakerAddress.stateAbbr(),
            country = defaultAddress[country]?.country ?: fakerAddress.country(),
            // countryCode from javaFaker does not support requirements format, convert to ISO 3166-1 alpha-3
            countryCode = defaultAddress[country]?.countryCode ?: CountryCode.getByCode(fakerAddress.countryCode()).alpha3
        )
        try {
            addressValidator.validate(address)
        } catch (ex: AddressValidationException) {
            logger.error("Failed to generate address with javaFaker. default to hardcoded address. errors: ${ex.message}")
            return defaultAddress[country] ?: Fixture.Address.unitedStates()
        }
        return address
    }

    /**
     * Fetch country based on countryAbbr or pick a random country from a supported list
     * internal for unit test visibility
     */
    @Throws(IncorrectCountryCodeException::class)
    internal fun getCountry(countryAbbr: String?): Country {
        // if there is a request value and value supported
        val country = Country.fromName(countryAbbr)
        if (countryAbbr != null && country != null) {
            return country
        }
        // if there is a request value and it is not supported, notify user
        if (countryAbbr != null && country == null) {
            throw IncorrectCountryCodeException("countryCode is incorrect. Supported values: [${Country.abbreviations()}]")
        }
        // if there is no request value pick a random country
        return when (getRandomNumber()) {
            0 -> Country.US
            1 -> Country.CANADA
            2 -> Country.MEXICO
            3 -> Country.NETHERLANDS
            else -> Country.US
        }
    }

    /**
     * Returns a random number in the range between 0 and 3
     * Moved to a separate method to control behavior from unit tests
     * internal for unit test visibility
     */
    internal fun getRandomNumber() = IntRange(0, 3).random()
}
