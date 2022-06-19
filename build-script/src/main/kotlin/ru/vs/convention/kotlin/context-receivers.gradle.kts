package ru.vs.convention.kotlin

import org.gradle.kotlin.dsl.withType

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
}