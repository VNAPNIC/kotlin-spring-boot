//package com.vnapnic.com.vnapnic.discovery.config
//
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration
//import org.springframework.context.annotation.Primary
//import org.springframework.stereotype.Component
//import springfox.documentation.swagger.web.SwaggerResource
//import springfox.documentation.swagger.web.SwaggerResourcesProvider
//
//@Component
//@Primary
//@EnableAutoConfiguration
//class DocumentationConfig : SwaggerResourcesProvider{
//
//    override fun get(): List<SwaggerResource?> {
//        val resources: MutableList<SwaggerResource?> = arrayListOf()
//        resources.add(swaggerResource("auth-service", "/api/auth/v2/api-docs", "2.0"))
//        resources.add(swaggerResource("storage-service", "/api/storage/v2/api-docs", "2.0"))
//        resources.add(swaggerResource("user-service", "/api/user/v2/api-docs", "2.0"))
//        resources.add(swaggerResource("p2p-service", "/api/p2p/v2/api-docs", "2.0"))
//        return resources
//    }
//
//    private fun swaggerResource(name: String, location: String, version: String): SwaggerResource? {
//        val swaggerResource = SwaggerResource()
//        swaggerResource.name = name
//        swaggerResource.location = location
//        swaggerResource.swaggerVersion = version
//        return swaggerResource
//    }
//}