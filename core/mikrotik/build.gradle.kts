plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":core:di"))
                implementation(project(":core:ktor-network"))
            }
        }
    }
}
