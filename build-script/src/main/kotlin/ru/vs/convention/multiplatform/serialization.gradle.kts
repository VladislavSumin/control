package ru.vs.convention.multiplatform

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.the

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

val libs = the<LibrariesForLibs>()

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(libs.kotlin.serialization.core)
            }
        }
    }
}
