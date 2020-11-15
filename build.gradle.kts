import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.kotlin.dsl.version
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val springBootVersion = "2.2.5.RELEASE"
    val junitPluginVersion = "1.0.9.RELEASE"
    project.extra.set("springBootVersion", springBootVersion)

    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
        classpath("io.spring.gradle:dependency-management-plugin:$junitPluginVersion")
    }
}


plugins {
    val kotlinVersion = "1.3.72"
    val springBootVersion = "2.2.5.RELEASE"
    val dependencyManagementVersion = "1.0.10.RELEASE"
    base
    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
    kotlin("plugin.jpa") version kotlinVersion apply false
    id("org.springframework.boot") version springBootVersion apply false
    id("io.spring.dependency-management") version dependencyManagementVersion
}

extra["swaggerVersion"] = "2.9.2"

allprojects {
    group = "com.vnapnic"

    println("Enabling Spring Boot Dependency Management in project ${project.name}...")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        jcenter()
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("org.jetbrains.kotlin.kapt")
        plugin("org.springframework.boot")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.jetbrains.kotlin.plugin.jpa")
    }
    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    tasks.withType<Jar> {
        enabled = true
    }

    tasks.withType<KotlinCompile>().configureEach {
        println("Configuring $name in project ${project.name}...")
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        "api"(kotlin("stdlib-jdk8"))
        "api"(kotlin("reflect"))
        "api"("org.jetbrains.kotlin:kotlin-reflect")
        "api"("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        "testImplementation"("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }
    }

    configure<DependencyManagementExtension> {
        imports {
            val springBootVersion = parent!!.extra.get("springBootVersion")
            val springCloudVersion = "Hoxton.SR9"
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
            mavenBom("org.springframework.boot:spring-boot-parent:$springBootVersion")
        }
    }
}