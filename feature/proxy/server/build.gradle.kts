plugins {
    id("ru.vs.convention.feature.server")
}

kotlin {
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(project(":core:ktor-client"))
                implementation("io.ktor:ktor-network-tls-certificates:${libs.versions.ktor.get()}")
            }
        }
    }
}
