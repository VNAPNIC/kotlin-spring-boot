extra["springBootAdminVersion"] = "2.3.0"

dependencies {
    implementation(project(":service-common"))

    implementation("de.codecentric:spring-boot-admin-starter-server")
    implementation("de.codecentric:spring-boot-admin-server-ui")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
}

dependencyManagement {
    imports {
        mavenBom("de.codecentric:spring-boot-admin-dependencies:${property("springBootAdminVersion")}")
    }
}