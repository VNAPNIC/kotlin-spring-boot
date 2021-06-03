package com.vnapnic.gateway

import com.vnapnic.gateway.filter.JwtFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean
import springfox.documentation.swagger.web.*
import springfox.documentation.swagger2.annotations.EnableSwagger2


@ConfigurationPropertiesScan
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EnableHystrix
@EnableSwagger2
class GatewayApplication {

    @Bean
    fun uiConfig(): UiConfigurationBuilder? {
        return UiConfigurationBuilder.builder()
                .validatorUrl("validatorUrl")
                .docExpansion(DocExpansion.LIST)
                .tagsSorter(TagsSorter.ALPHA)
                .defaultModelRendering(ModelRendering.of("schema"))
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
    }
}

@Bean
fun jwtFilter(): JwtFilter {
    return JwtFilter()
}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}


