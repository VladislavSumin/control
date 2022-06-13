plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.ktor.network.core)
                implementation(project(":core:coroutines"))
                implementation(project(":core:di"))
            }
        }
    }
}
