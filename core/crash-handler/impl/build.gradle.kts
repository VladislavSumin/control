plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(project(":core:crash-handler:api"))
                implementation(project(":core:logging"))
            }
        }
    }
}
