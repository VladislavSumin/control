plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

kotlin {
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(project(":core:coroutines"))
                implementation(project(":core:logging-slf4j"))
            }
        }
    }
}
