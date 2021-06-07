package com.vnapnic.gateway

//import com.vnapnic.gateway.filter.JwtFilter
//import org.springframework.boot.context.properties.ConfigurationPropertiesScan
//import org.springframework.context.annotation.Bean
//import springfox.documentation.swagger.web.*

import JwtFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.context.annotation.Bean
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.swagger.web.*
import springfox.documentation.swagger2.annotations.EnableSwagger2


@ConfigurationPropertiesScan
@SpringBootApplication
@EnableEurekaClient
//@EnableHystrix
//@EnableSwagger2
class GatewayApplication {

//    @Bean
//    fun uiConfig(): UiConfigurationBuilder? {
//        return UiConfigurationBuilder.builder()
//                .validatorUrl("validatorUrl")
//                .docExpansion(DocExpansion.LIST)
//                .tagsSorter(TagsSorter.ALPHA)
//                .defaultModelRendering(ModelRendering.of("schema"))
//                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
//    }

    @Bean
    fun serverCodecConfigurer(): ServerCodecConfigurer? {
        return ServerCodecConfigurer.create()
    }


}

@RestController
@RequestMapping()
public class UserController {
    @GetMapping("/api/login")
    @ResponseBody
    fun login(): String? {
        return "还没有登录页面"
    }
}

@Bean
fun jwtFilter(): JwtFilter {
    return JwtFilter()
}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}


