package com.generator.demo.controller

import com.generator.demo.data.Fixture
import com.generator.demo.dto.Address
import com.generator.demo.enum.Country
import com.generator.demo.service.AddressRandomizerService
import com.generator.demo.service.IncorrectCountryCodeException
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.web.server.ResponseStatusException

class AddressRandomizerControllerTest {

    private lateinit var addressRandomizerController: AddressRandomizerController
    private lateinit var addressRandomizerService: AddressRandomizerService

    @BeforeEach
    fun setup() {
        addressRandomizerService = mock()

        addressRandomizerController = AddressRandomizerController(addressRandomizerService)
    }

    @Nested
    internal inner class RandomizeAddress {

        private lateinit var address: Address

        @BeforeEach
        fun setup() {
            address = Fixture.Address.unitedStates()
        }

        @Test
        fun `randomizeAddress with no parameters`() {
            val countryAbbr = null
            doReturn(address).whenever(addressRandomizerService).generateAddress(countryAbbr)

            val response = addressRandomizerController.randomizeAddress(countryAbbr)
            Assertions.assertEquals(response.statusCode, org.springframework.http.HttpStatus.OK)
            Assertions.assertEquals(response.body, address)
        }

        @Test
        fun `randomizeAddress with an expected query parameter`() {
            val countryAbbr = Country.NETHERLANDS.countryAbbr
            doReturn(address).whenever(addressRandomizerService).generateAddress(countryAbbr)

            val response = addressRandomizerController.randomizeAddress(countryAbbr)
            Assertions.assertEquals(response.statusCode, org.springframework.http.HttpStatus.OK)
            Assertions.assertEquals(response.body, address)
        }

        @Test
        fun `randomizeAddress with an unexpected query parameter`() {
            val serviceException = IncorrectCountryCodeException("message")
            val countryAbbr = "Unknown"
            doThrow(serviceException).whenever(addressRandomizerService).generateAddress(countryAbbr)

            Assertions.assertThrows(ResponseStatusException::class.java) {
                addressRandomizerController.randomizeAddress(countryAbbr)
            }
        }
    }
}
