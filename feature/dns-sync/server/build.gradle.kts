plugins {
    id("ru.vs.convention.feature.server")
}

kotlin {
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(project(":core:mikrotik"))
            }
        }
    }
}
