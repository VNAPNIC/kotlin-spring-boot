dependencies {
    implementation(project(":service-common"))

    implementation("org.springframework.cloud:spring-cloud-config-client")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    //security
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security.oauth:spring-security-oauth2:2.3.5.RELEASE")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-mail")
}