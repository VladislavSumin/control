plugins {
    id("ru.vs.convention.multiplatform.jvm")
    id("ru.vs.convention.multiplatform.serialization")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.kotlin.serialization.json)
                implementation(project(":core:di"))
            }
        }
    }
}
