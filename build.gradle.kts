import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer

plugins {
	java
	id("org.springframework.boot") version "3.1.1"
	id("io.spring.dependency-management") version "1.1.0"
	id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "cz.klecansky"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

val springCloudFunctionVersion = "4.0.4"
val springCloudAwsVersion = "3.0.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.cloud:spring-cloud-function-context:${springCloudFunctionVersion}")
	implementation("org.springframework.cloud:spring-cloud-function-adapter-aws:${springCloudFunctionVersion}")
	implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:${springCloudAwsVersion}"))
	implementation("io.awspring.cloud:spring-cloud-aws-starter-ses")
	implementation("com.amazonaws:aws-lambda-java-events:3.9.0")
	implementation("org.springframework.boot:spring-boot-configuration-processor:3.0.5")
	implementation("org.jsoup:jsoup:1.16.1")
	implementation("org.javamoney:moneta:1.4.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	compileOnly("com.amazonaws:aws-lambda-java-core:1.1.0")

}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks {
	named<ShadowJar>("shadowJar") {
		archiveBaseName.set("aws")
		dependencies {
			exclude(dependency("org.springframework.cloud:spring-cloud-function-web:${springCloudFunctionVersion}"))
		}
		mergeServiceFiles()
		append("META-INF/spring.handlers")
		append("META-INF/spring.schemas")
		append("META-INF/spring.tooling")
		append("META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports")
		append("META-INF/spring/org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration.imports")
		transform(PropertiesFileTransformer::class.java) {
			paths = listOf("META-INF/spring.factories")
			mergeStrategy = "append"
		}
	}
}

tasks {
	assemble {
		dependsOn(shadowJar)
	}
}
