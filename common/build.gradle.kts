dependencies {
    api("com.fasterxml.jackson.datatype:jackson-datatype-joda:2.8.5")

    implementation("com.google.code.gson:gson:2.7")
    api("io.jsonwebtoken:jjwt:0.9.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
}