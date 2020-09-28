package com.generator.demo.service

import com.generator.demo.enum.Country
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class AddressRandomizerServiceTest {

    private lateinit var addressValidator: AddressValidator
    private lateinit var addressRandomizerService: AddressRandomizerService

    @BeforeEach
    fun setup() {
        addressValidator = mock()

        addressRandomizerService = spy(AddressRandomizerService(addressValidator))
    }

    @Nested
    internal inner class GenerateAddress {

        @BeforeEach
        fun setup() {
            doNothing().whenever(addressValidator).validate(any())
        }

        @Test
        fun `empty countryAbbr value`() {
            val address = addressRandomizerService.generateAddress(null)

            Assertions.assertNotNull(address.house)
            Assertions.assertNotNull(address.street)
            Assertions.assertNotNull(address.postalCode)
            Assertions.assertNotNull(address.city)
            Assertions.assertNotNull(address.county)
            Assertions.assertNotNull(address.state)
            Assertions.assertNotNull(address.stateCode)
            Assertions.assertNotNull(address.country)
            Assertions.assertNotNull(address.countryCode)
        }

        @Test
        fun `unsupported countryAbbr value`() {
            Assertions.assertThrows(IncorrectCountryCodeException::class.java) {
                addressRandomizerService.generateAddress("Oops")
            }
        }

        @Test
        fun `supported countryAbbr value`() {
            val address = addressRandomizerService.getCountry("MX")

            Assertions.assertNotNull(address)
        }

        @Test
        fun `success default result if validator returns failures`() {
            val ex = AddressValidationException("Oops")
            doThrow(ex).whenever(addressValidator).validate(any())
            val address = addressRandomizerService.generateAddress(null)

            Assertions.assertNotNull(address.house)
            Assertions.assertNotNull(address.street)
            Assertions.assertNotNull(address.postalCode)
            Assertions.assertNotNull(address.city)
            Assertions.assertNotNull(address.county)
            Assertions.assertNotNull(address.state)
            Assertions.assertNotNull(address.stateCode)
            Assertions.assertNotNull(address.country)
            Assertions.assertNotNull(address.countryCode)
        }
    }

    @Nested
    internal inner class GetCountry {

        @Test
        fun `supported countryAbbr value`() {
            val country = addressRandomizerService.getCountry("MX")

            Assertions.assertEquals(Country.MEXICO, country)
        }

        @Test
        fun `unsupported countryAbbr value`() {
            Assertions.assertThrows(IncorrectCountryCodeException::class.java) {
                addressRandomizerService.getCountry("Oops")
            }
        }

        @Test
        fun `empty countryAbbr value`() {
            doReturn(1).whenever(addressRandomizerService).getRandomNumber()
            val country = addressRandomizerService.getCountry(null)

            Assertions.assertEquals(Country.CANADA, country)
        }

        @Test
        fun `empty countryAbbr value with US choice`() {
            doReturn(0).whenever(addressRandomizerService).getRandomNumber()
            val country = addressRandomizerService.getCountry(null)

            Assertions.assertEquals(Country.US, country)
        }

        @Test
        fun `empty countryAbbr value with CANADA choice`() {
            doReturn(1).whenever(addressRandomizerService).getRandomNumber()
            val country = addressRandomizerService.getCountry(null)

            Assertions.assertEquals(Country.CANADA, country)
        }

        @Test
        fun `empty countryAbbr value with MEXICO choice`() {
            doReturn(2).whenever(addressRandomizerService).getRandomNumber()
            val country = addressRandomizerService.getCountry(null)

            Assertions.assertEquals(Country.MEXICO, country)
        }

        @Test
        fun `empty countryAbbr value with NETHERLANDS choice`() {
            doReturn(3).whenever(addressRandomizerService).getRandomNumber()
            val country = addressRandomizerService.getCountry(null)

            Assertions.assertEquals(Country.NETHERLANDS, country)
        }

        @Test
        fun `empty countryAbbr value with other choice`() {
            doReturn(10).whenever(addressRandomizerService).getRandomNumber()
            val country = addressRandomizerService.getCountry(null)

            Assertions.assertEquals(Country.US, country)
        }
    }
}
