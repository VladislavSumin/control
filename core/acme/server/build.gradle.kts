plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(project(":core:acme:core"))
                implementation(project(":core:ktor-server"))
            }
        }
    }
}
