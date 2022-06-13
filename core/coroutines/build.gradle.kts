plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.kotlin.coroutines.core)
                implementation(project(":core:di"))
            }
        }
    }
}
