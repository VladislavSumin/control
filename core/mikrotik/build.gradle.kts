plugins {
    id("ru.vs.convention.multiplatform.jvm")
    id("ru.vs.convention.kotlin.context-receivers")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":core:di"))
                implementation(project(":core:ktor-network"))
                implementation(project(":core:logging"))
            }
        }
    }
}
