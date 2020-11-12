plugins {
    kotlin("jvm")
}

dependencies {

    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))


    api("org.springframework.boot:spring-boot-starter-data-rest")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-actuator")
    api("org.springframework.boot:spring-boot-starter-security")

    api("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("org.jetbrains.kotlin:kotlin-reflect")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}