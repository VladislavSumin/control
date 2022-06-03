plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {

        named("jvmMain") {
            dependencies {
                implementation("io.ktor:ktor-network-tls-certificates:${libs.versions.ktor.get()}")
            }
        }
    }
}
