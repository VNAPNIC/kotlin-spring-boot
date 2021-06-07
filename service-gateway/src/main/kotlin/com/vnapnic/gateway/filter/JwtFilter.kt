import com.fasterxml.jackson.databind.ObjectMapper
import com.vnapnic.common.entities.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered

@Component
class JwtFilter : GlobalFilter , Ordered {
    private val log = LoggerFactory.getLogger(JwtFilter::class.java)

    @Autowired
    lateinit var stringRedisTemplate: StringRedisTemplate

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val response = exchange.response
        val request = exchange.request
        //Get the request path, if it is auth, swagger-ui, api-docs request, let it go directly
        val path = request.uri.path

        log.info("--------------------------> $path")

        if (path.indexOf("/auth/") <= 0
                && path.indexOf("/swagger-ui") <= 0
                && path.indexOf("/api-docs") <= 0
        ) {
            return chain.filter(exchange)
        }

        val mapper = ObjectMapper()

        val accessToken = request.headers["Authorization"]

        log.info("--------------------------> $accessToken")

        if (accessToken.isNullOrEmpty()){
            val dataBufferFactory = exchange.response.bufferFactory()
            response.statusCode = HttpStatus.UNAUTHORIZED
            val bytes = mapper.writeValueAsBytes(Response.unauthorized())
            return response.writeWith(Mono.just(bytes).map { r-> dataBufferFactory.wrap(r) })
        }
        return chain.filter(exchange)
    }

    override fun getOrder(): Int = 0
}

//package com.vnapnic.gateway.filter
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.netflix.zuul.ZuulFilter
//import com.netflix.zuul.context.RequestContext
//import com.vnapnic.common.entities.Response
//import com.vnapnic.common.service.JWTService
//import io.jsonwebtoken.ExpiredJwtException
//import io.jsonwebtoken.MalformedJwtException
//import io.jsonwebtoken.SignatureException
//import io.jsonwebtoken.UnsupportedJwtException
//import org.apache.http.HttpStatus
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Component
//import org.springframework.web.client.RestTemplate
//
//@Component
//class JwtFilter : ZuulFilter() {
//    private val log = LoggerFactory.getLogger(JwtFilter::class.java)
//
//    @Autowired
//    lateinit var restTemplate: RestTemplate
//
//    @Autowired
//    lateinit var jwtService: JWTService
//
//    override fun shouldFilter(): Boolean {
//        val context = RequestContext.getCurrentContext()
//        val request = context.request
//        val requestUrl = request.requestURL.toString()
//        return requestUrl.indexOf("/auth/") <= 0
//                && requestUrl.indexOf("/swagger-ui") <=0
//                && requestUrl.indexOf("/api-docs") <=0
//    }
//
//    override fun filterType(): String = "pre"
//
//    override fun filterOrder(): Int = 0
//
//    override fun run(): Any? {
//        val context = RequestContext.getCurrentContext()
//        val request = context.request
//        val mapper = ObjectMapper()
//
//        val accessToken = request.getHeader("Authorization")
//        if (accessToken.isNullOrEmpty() || !accessToken.startsWith("Bearer")) {
//            log.warn("Missing Authorization header and token.")
//            context.setSendZuulResponse(false)
//            context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
//            context.responseBody = mapper.writeValueAsString(Response.unauthorized())
//        } else {
//            try {
//                val token: String = accessToken.replace("Bearer ", "")
//                log.info("Token - $token")
//                jwtService.parseJWT(token)
//            } catch (e: SignatureException) {
//                log.info("Invalid JWT signature.");
//                log.trace("Invalid JWT signature trace: {}", e)
//                context.setSendZuulResponse(false)
//                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
//                context.responseBody = mapper.writeValueAsString(Response.unauthorized(message = "Invalid JWT signature: {$e}"))
//            } catch (e: MalformedJwtException) {
//                log.info("Invalid JWT token.");
//                log.trace("Invalid JWT token trace: {}", e)
//                context.setSendZuulResponse(false)
//                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
//                context.responseBody = mapper.writeValueAsString(Response.unauthorized(message = "Invalid JWT token: {$e}"))
//            } catch (e: ExpiredJwtException) {
//                log.info("Expired JWT token.");
//                log.trace("Expired JWT token trace: {}", e)
//                context.setSendZuulResponse(false)
//                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
//                context.responseBody = mapper.writeValueAsString(Response.unauthorized(message = "Expired JWT token: {$e}"))
//            } catch (e: UnsupportedJwtException) {
//                log.info("Unsupported JWT token.");
//                log.trace("Unsupported JWT token trace: {}", e)
//                context.setSendZuulResponse(false)
//                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
//                context.responseBody = mapper.writeValueAsString(Response.unauthorized(message = "Unsupported JWT token: {$e}"))
//            } catch (e: IllegalArgumentException) {
//                log.info("JWT token compact of handler are invalid.");
//                log.trace("JWT token compact of handler are invalid trace: {}", e)
//                context.setSendZuulResponse(false)
//                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
//                context.responseBody = mapper.writeValueAsString(Response.unauthorized(message = "JWT token compact of handler are invalid trace: {$e}"))
//            } catch (e: Exception) {
//                e.printStackTrace()
//                log.warn("Can't parse JWT token.")
//                context.setSendZuulResponse(false)
//                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
//                context.responseBody = mapper.writeValueAsString(Response.unauthorized())
//            }
//        }
//
//        log.info(String.format("%s request to %s", request.method, request.requestURL.toString()))
//        return null
//    }
//}