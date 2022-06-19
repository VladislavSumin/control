plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(project(":core:utils-network"))
                api(libs.ktor.client.core)

                implementation(project(":core:di"))
            }
        }
        named("jvmMain") {
            dependencies {
                api(libs.ktor.client.okhttp)
            }
        }
    }
}
