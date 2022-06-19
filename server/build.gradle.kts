plugins {
    id("ru.vs.convention.multiplatform.jvm")
    id("ru.vs.convention.kotlin.context-receivers")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":core:coroutines"))
                implementation(project(":core:di"))
                implementation(project(":core:ktor-client"))
                implementation(project(":core:ktor-network"))
                implementation(project(":core:ktor-server"))
                implementation(project(":core:logging-slf4j"))
                implementation(project(":core:mikrotik"))
                implementation(project(":core:sentry"))
                implementation(project(":core:serialization"))

                implementation(project(":feature:dns-sync:server"))
                implementation(project(":feature:proxy:server"))
            }
        }
    }
}
