package ru.vs.convention.feature

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("ru.vs.convention.multiplatform.jvm")
}

val libs = the<LibrariesForLibs>()

kotlin {
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(project(":core:coroutines"))
                implementation(project(":core:di"))
                implementation(project(":core:logging"))
            }
        }
    }
}