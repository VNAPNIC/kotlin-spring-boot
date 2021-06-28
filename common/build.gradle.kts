dependencies {
    api(project(":database"))
    api("org.springframework.cloud:spring-cloud-starter-netflix-zuul")
    api("com.fasterxml.jackson.datatype:jackson-datatype-joda:2.8.5")
    api("io.springfox:springfox-swagger2:2.9.2")
    api("io.springfox:springfox-swagger-ui:2.9.2")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.boot:spring-boot-starter-validation")
}