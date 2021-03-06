package ru.vs.convention.feature

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("ru.vs.convention.multiplatform.jvm")
    id("ru.vs.convention.multiplatform.serialization")
}

val libs = the<LibrariesForLibs>()

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":core:coroutines"))
                implementation(project(":core:crash-handler:api"))
                implementation(project(":core:di"))
                implementation(project(":core:ktor-server"))
                implementation(project(":core:logging"))
                implementation(project(":core:serialization"))
            }
        }
    }
}