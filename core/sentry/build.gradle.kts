plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.sentry.core)

                implementation(project(":core:crash-handler:impl"))
                implementation(project(":core:di"))
            }
        }
    }
}
