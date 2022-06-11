plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(project(":core:coroutines"))
                implementation(project(":core:di"))
                implementation(project(":core:ktor-client"))
                implementation(project(":core:ktor-server"))
                implementation(project(":core:logging-slf4j"))

                implementation(project(":feature:proxy:server"))

                implementation(libs.sentry.core)
            }
        }
    }
}
