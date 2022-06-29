plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(project(":core:acme:server:core"))
            }
        }
    }
}
