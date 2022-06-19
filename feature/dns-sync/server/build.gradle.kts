plugins {
    id("ru.vs.convention.feature.server")
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
