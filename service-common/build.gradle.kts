dependencies {

    api("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    api("org.springframework.boot:spring-boot-starter-data-rest")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-data-jpa")

    api("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("mysql:mysql-connector-java")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}