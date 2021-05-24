dependencies {
    api("com.fasterxml.jackson.datatype:jackson-datatype-joda:2.8.5")
    api(project(":database"))
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
}