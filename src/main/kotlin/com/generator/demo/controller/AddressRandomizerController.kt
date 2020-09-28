package com.generator.demo.controller

import com.generator.demo.dto.Address
import com.generator.demo.service.AddressRandomizerService
import com.generator.demo.service.IncorrectCountryCodeException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class AddressRandomizerController(
    private val addressRandomizerService: AddressRandomizerService
) {

    val logger: Logger = LoggerFactory.getLogger(AddressRandomizerController::class.java)

    /**
     * Generate a random address
     * Endpoint takes countryAbbr as a query parameter
     * Query parameter values are based on countryAbrr from com.generator.demo.enum.Country enum values
     */
    @GetMapping(
        "/randomizer/address",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun randomizeAddress(@RequestParam countryAbbr: String?): ResponseEntity<Address> {
        logger.info("Generate a random address")
        val address = try {
            addressRandomizerService.generateAddress(countryAbbr)
        } catch (ex: IncorrectCountryCodeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message)
        }
        return ResponseEntity(address, HttpStatus.OK)
    }
}
