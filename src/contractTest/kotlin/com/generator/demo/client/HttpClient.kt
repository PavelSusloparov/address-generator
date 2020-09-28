package com.generator.demo.client

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import kotlin.reflect.KClass

@Component
class HttpClient(
    private val restTemplate: RestTemplate,
    private var environment: Environment,
    private val objectMapper: ObjectMapper
) {
    val logger: Logger = LoggerFactory.getLogger(HttpClient::class.java)

    // The application instance runs on a random port
    // local.server.port is set during the test application bootstrap
    val port: String by lazy { environment.getProperty("local.server.port")!! }
    var host = "http://localhost:"

    fun <T : Any> get(requestUrl: String, clazz: KClass<T>): T {
        val response = sendRequest(HttpMethod.GET, "$host$port/$requestUrl", null)
        return objectMapper.convertValue(response.body!!, clazz.java)
    }

    fun <T> sendRequest(
        method: HttpMethod,
        requestUrl: String,
        requestBody: T?
    ): ResponseEntity<JsonNode> {
        val httpHeaders = HttpHeaders()
        httpHeaders.set("Content-Type", "application/json")
        val request = HttpEntity(requestBody, httpHeaders)
        try {
            logger.info("HttpClient.sendRequest(): requestUrl $requestUrl")
            return restTemplate.exchange(requestUrl, method, request, JsonNode::class.java)
        } catch (ex: HttpClientErrorException) {
            logger.error("HttpClientErrorException - statusCode: ${ex.statusCode}, request - $method $requestUrl")
            throw ex
        } catch (ex: HttpServerErrorException) {
            logger.error("HttpServerErrorException - statusCode: ${ex.statusCode}, request - $method $requestUrl")
            throw ex
        } catch (ex: Exception) {
            throw ex
        }
    }
}
