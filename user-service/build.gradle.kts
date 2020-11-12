plugins {
    kotlin("jvm")
}

dependencies {

    api(project(":common-service"))

    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    //jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    //Oauth2
    implementation("org.springframework.security.oauth:spring-security-oauth2:2.3.5.RELEASE")
    implementation("org.springframework.security.oauth:spring-security-oauth2")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("mysql:mysql-connector-java")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
}