plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":core:acme:server:ktor"))
                implementation(project(":core:coroutines"))
                implementation(project(":core:di"))
                implementation(project(":core:ktor-client"))
                implementation(project(":core:ktor-network"))
                implementation(project(":core:ktor-server"))
                implementation(project(":core:logging-slf4j"))
                implementation(project(":core:serialization"))
                implementation(libs.ktor.server.callLogging)
                implementation(libs.ktor.server.contentNegotiation)
                implementation(libs.ktor.serialization.json)
            }
        }
    }
}
