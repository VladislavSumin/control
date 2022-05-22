plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("jvmMain") {
            dependencies {
                api(libs.ktor.server.netty)
            }
        }
    }
}
