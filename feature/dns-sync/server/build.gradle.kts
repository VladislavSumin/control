plugins {
    id("ru.vs.convention.feature.server")
    id("ru.vs.convention.kotlin.context-receivers")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":core:mikrotik"))
            }
        }
    }
}
