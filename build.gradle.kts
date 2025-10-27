val kotlin_version: String by project
val kotlinx_html_version: String by project
val logback_version: String by project

plugins {
	kotlin("jvm") version "2.2.20"
	id("io.ktor.plugin") version "3.3.1"
}

group = "site.remlit"
version = "0.0.1"

application {
	mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {
	implementation("io.ktor:ktor-server-core")
	implementation("com.ucasoft.ktor:ktor-simple-cache:0.55.3")
	implementation("com.ucasoft.ktor:ktor-simple-memory-cache:0.55.3")
	implementation("io.ktor:ktor-server-host-common")
	implementation("io.ktor:ktor-server-call-logging")
	implementation("io.ktor:ktor-server-html-builder")
	implementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinx_html_version")
	implementation("io.ktor:ktor-server-netty")
	implementation("io.ktor:ktor-server-config-yaml")
	implementation("ch.qos.logback:logback-classic:${logback_version}")
	implementation("org.jetbrains:markdown:0.7.3")

	testImplementation("io.ktor:ktor-server-test-host")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
