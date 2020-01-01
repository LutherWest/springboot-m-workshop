plugins {
    java
    id("org.springframework.boot") version "2.2.2.RELEASE"
}

apply(plugin = "io.spring.dependency-management")

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":workshop-starter"))
    implementation("org.springframework.boot:spring-boot-starter-quartz")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(project(":workshop-starter"))

    compileOnly("org.projectlombok:lombok")
}
