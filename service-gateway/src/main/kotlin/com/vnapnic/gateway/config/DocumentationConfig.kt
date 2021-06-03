package com.vnapnic.gateway.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import springfox.documentation.swagger.web.SwaggerResource
import springfox.documentation.swagger.web.SwaggerResourcesProvider
import java.util.*


@Component
@Primary
@EnableAutoConfiguration
class DocumentationConfig : SwaggerResourcesProvider{

    override fun get(): List<SwaggerResource?> {
        val resources: MutableList<SwaggerResource?> = arrayListOf()
        resources.add(swaggerResource("auth-service", "/api/auth/v2/api-docs", "2.0"))
        resources.add(swaggerResource("storage-service", "/api/storage/v2/api-docs", "2.0"))
        resources.add(swaggerResource("user-service", "/api/user/v2/api-docs", "2.0"))
        resources.add(swaggerResource("p2p-service", "/api/p2p/v2/api-docs", "2.0"))
        return resources
    }

    private fun swaggerResource(name: String, location: String, version: String): SwaggerResource? {
        val swaggerResource = SwaggerResource()
        swaggerResource.name = name
        swaggerResource.location = location
        swaggerResource.swaggerVersion = version
        return swaggerResource
    }
}

//@Configuration
//@EnableSwagger2
//class DocumentationConfig {
//
//    @Autowired
//    lateinit var properties: ZuulProperties
//
//    @Primary
//    @Bean
//    fun swaggerResourcesProvider(): SwaggerResourcesProvider {
//        return SwaggerResourcesProvider {
//            val resources: MutableList<SwaggerResource> = arrayListOf()
//            properties.routes.values.stream()
//                    .forEach { route: ZuulRoute -> resources.add(createResource(route.id, "2.0")) }
//            resources
//        }
//    }
//
//    private fun createResource(location: String?, version: String?): SwaggerResource {
//        val swaggerResource = SwaggerResource()
//        swaggerResource.name = location
//        swaggerResource.location = "/$location/v1/api-docs"
//        swaggerResource.swaggerVersion = version
//        return swaggerResource
//    }
//
//    @Bean
//    open fun api(): Docket? {
//        return Docket(DocumentationType.SWAGGER_2).select()
//                .apis(RequestHandlerSelectors.basePackage("com.vnapnic.*"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiEndPointsInfo())
//    }
//
//    private fun apiEndPointsInfo(): ApiInfo? {
//        return ApiInfoBuilder().title("Learn english by communicate REST API")
//                .description("Learn english by communicate REST API")
//                .contact(Contact("vnapnic", "https://buihainam.com/", "nankai1421@gmail.com"))
//                .license("Apache 2.0")
//                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
//                .version("1.0.0")
//                .build()
//    }
//}