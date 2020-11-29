dependencies {
    api(project(":service-common"))
    api("io.jsonwebtoken:jjwt:0.9.1")
    //security
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.security.oauth:spring-security-oauth2:2.3.5.RELEASE")
    api("org.springframework.security:spring-security-oauth2-resource-server")
    api("org.springframework.boot:spring-boot-starter-security")
}