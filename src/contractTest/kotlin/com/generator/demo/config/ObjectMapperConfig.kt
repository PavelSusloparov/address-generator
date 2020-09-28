package com.generator.demo.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfig {

    private val classLoader = object {}.javaClass.classLoader

    fun addModules(mapper: ObjectMapper) {
        val modules = ObjectMapper.findModules(classLoader)
        modules.forEach { mapper.registerModule(it) }
    }

    fun configure(mapper: ObjectMapper): ObjectMapper {
        addModules(mapper)
        mapper
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
        return mapper
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        configure(objectMapper)
        return objectMapper
    }
}
