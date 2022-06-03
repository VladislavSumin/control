plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":core:di"))
                api(libs.ktor.client.core)
            }
        }
        named("jvmMain") {
            dependencies {
                api(project(":core:utils-network"))
                api(libs.ktor.client.okhttp)
            }
        }
    }
}
