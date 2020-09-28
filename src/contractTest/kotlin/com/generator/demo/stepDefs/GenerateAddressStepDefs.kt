package com.generator.demo.stepDefs

import com.generator.demo.client.HttpClient
import com.generator.demo.dto.Address
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

data class AddressContext(
    var address: Address?,
    var exception: Exception?
)

class GenerateAddressStepDefs(
    private val httpClient: HttpClient
) {

    private lateinit var addressContextThread: ThreadLocal<AddressContext>

    @Before
    fun setup() {
        addressContextThread = ThreadLocal()
    }

    @When("user requests address generation for (.*)$")
    fun `user request address generation`(countryAbbr: String) {
        val queryParams = if (countryAbbr != "null") {
            "?countryAbbr=$countryAbbr"
        } else {
            ""
        }
        val addressContext = try {
            val address = httpClient.get("/randomizer/address$queryParams", Address::class)
            AddressContext(address = address, exception = null)
        } catch (ex: Exception) {
            AddressContext(address = null, exception = ex)
        }
        addressContextThread.set(addressContext)
    }

    @Then("address information returns with {int} http code")
    fun `address information returns with code`(httpCode: Int) {
        val addressContext = addressContextThread.get()
        val httpStatus = HttpStatus.valueOf(httpCode)
        if (HttpStatus.OK == httpStatus) {
            Assertions.assertNotNull(addressContext.address)
        } else if (HttpStatus.BAD_REQUEST == httpStatus) {
            Assertions.assertNull(addressContext.address)
            Assertions.assertTrue(addressContext.exception is HttpClientErrorException)
        }
    }
}
