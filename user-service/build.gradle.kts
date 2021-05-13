dependencies {
    implementation(project(":common"))

    implementation("org.springframework.cloud:spring-cloud-config-client")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.session:spring-session-data-redis")
    implementation("org.springframework.data:spring-data-redis")
}