plugins {
	java
	checkstyle
	jacoco
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "6.25.0"
}

group = "hse"
version = "0.0.1-SNAPSHOT"

checkstyle {
	toolVersion = "10.13.0"
	isIgnoreFailures = false
	maxWarnings = 0
	maxErrors = 0
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

spotless {
    java {
        googleJavaFormat()
    }
}

checkstyle {
    toolVersion = "10.13.0"
    isIgnoreFailures = false
    maxWarnings = 0
    maxErrors = 0
}


configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310");
}

tasks.test {
    useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport) // report is always generated after tests ru
}
tasks.jacocoTestReport {
	dependsOn(tasks.test)
    reports {
        html.required = true
    }
}
