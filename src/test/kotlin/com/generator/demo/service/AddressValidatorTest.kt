package com.generator.demo.service

import com.generator.demo.data.Fixture
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class AddressValidatorTest {

    private lateinit var addressValidator: AddressValidator

    @BeforeEach
    fun setup() {
        addressValidator = AddressValidator()
    }

    @Test
    fun `valid address`() {
        val address = Fixture.Address.unitedStates()
        addressValidator.validate(address)

        Assertions.assertTrue(addressValidator.errorList.isEmpty())
    }

    @Test
    fun `invalid house field`() {
        val address = Fixture.Address.unitedStates().apply { house = "#" }
        Assertions.assertThrows(AddressValidationException::class.java) {
            addressValidator.validate(address)
        }
    }

    @Test
    fun `invalid street field`() {
        val address = Fixture.Address.unitedStates().apply { street = "#" }
        Assertions.assertThrows(AddressValidationException::class.java) {
            addressValidator.validate(address)
        }
    }

    @Test
    fun `invalid postalCode field`() {
        val address = Fixture.Address.unitedStates().apply { postalCode = "#" }
        Assertions.assertThrows(AddressValidationException::class.java) {
            addressValidator.validate(address)
        }
    }

    @Test
    fun `invalid city field`() {
        val address = Fixture.Address.unitedStates().apply { city = "#" }
        Assertions.assertThrows(AddressValidationException::class.java) {
            addressValidator.validate(address)
        }
    }

    @Test
    fun `invalid county field`() {
        val address = Fixture.Address.unitedStates().apply { county = "#" }
        Assertions.assertThrows(AddressValidationException::class.java) {
            addressValidator.validate(address)
        }
    }

    @Test
    fun `invalid state field`() {
        val address = Fixture.Address.unitedStates().apply { state = "#" }
        Assertions.assertThrows(AddressValidationException::class.java) {
            addressValidator.validate(address)
        }
    }

    @Test
    fun `invalid stateCode field`() {
        val address = Fixture.Address.unitedStates().apply { stateCode = "#" }
        Assertions.assertThrows(AddressValidationException::class.java) {
            addressValidator.validate(address)
        }
    }

    @Test
    fun `invalid country field`() {
        val address = Fixture.Address.unitedStates().apply { country = "#" }
        Assertions.assertThrows(AddressValidationException::class.java) {
            addressValidator.validate(address)
        }
    }

    @Test
    fun `invalid countryCode field`() {
        val address = Fixture.Address.unitedStates().apply { countryCode = "#" }
        Assertions.assertThrows(AddressValidationException::class.java) {
            addressValidator.validate(address)
        }
    }

    @Test
    fun `empty countryCode field`() {
        val address = Fixture.Address.unitedStates().apply { countryCode = "" }
        Assertions.assertThrows(AddressValidationException::class.java) {
            addressValidator.validate(address)
        }
    }

    @Test
    fun `digits in city field`() {
        val address = Fixture.Address.unitedStates().apply { city = "123" }
        Assertions.assertThrows(AddressValidationException::class.java) {
            addressValidator.validate(address)
        }
    }
}
