package com.vnapnic.gateway.provider

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.SwaggerResource
import springfox.documentation.swagger.web.SwaggerResourcesProvider
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class DocumentationProvider {

    @Autowired
    lateinit var properties: ZuulProperties

    @Primary
    @Bean
    fun swaggerResourcesProvider(): SwaggerResourcesProvider {
        return SwaggerResourcesProvider {
            val resources: MutableList<SwaggerResource> = arrayListOf()
            properties.routes.values.stream()
                    .forEach { route: ZuulRoute -> resources.add(createResource(route.id, "2.0")) }
            resources
        }
    }

    private fun createResource(location: String?, version: String?): SwaggerResource {
        val swaggerResource = SwaggerResource()
        swaggerResource.name = location
        swaggerResource.location = "/$location/v1/api-docs"
        swaggerResource.swaggerVersion = version
        return swaggerResource
    }

    @Bean
    open fun api(): Docket? {
        return Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.vnapnic.*"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiEndPointsInfo())
    }

    private fun apiEndPointsInfo(): ApiInfo? {
        return ApiInfoBuilder().title("Learn english by communicate REST API")
                .description("Learn english by communicate REST API")
                .contact(Contact("vnapnic", "https://buihainam.com/", "nankai1421@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build()
    }
}