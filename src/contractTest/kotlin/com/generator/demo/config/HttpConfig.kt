package com.generator.demo.config

import org.apache.http.client.config.RequestConfig
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import org.zalando.logbook.Logbook
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor

@Configuration
class HttpConfig {

    /**
     * Configure logbook logging for request/responses
     */
    internal fun clientHttpRequestFactory(): ClientHttpRequestFactory {
        val config = RequestConfig.custom().build()
        val logbook = Logbook.builder().build()
        val client = HttpClientBuilder
            .create()
            .setDefaultRequestConfig(config)
            .addInterceptorFirst(LogbookHttpRequestInterceptor(logbook))
            .addInterceptorLast(LogbookHttpResponseInterceptor())
            .build()
        return HttpComponentsClientHttpRequestFactory(client)
    }

    /**
     * restTemplate factory configuration with logbook logger
     */
    @Bean
    fun cmpRestTemplate(): RestTemplate {
        val httpRequestFactory = clientHttpRequestFactory()
        return RestTemplate(httpRequestFactory)
    }
}
