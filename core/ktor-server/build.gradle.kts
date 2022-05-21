plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("jvmMain") {
            dependencies {
                // TODO тут подключать только сервер core без движка
                api(libs.ktor.server.netty)
            }
        }
    }
}
