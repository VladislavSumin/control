plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.ktor.server.netty)
                api(libs.ktor.server.websockets)
            }
        }
    }
}
