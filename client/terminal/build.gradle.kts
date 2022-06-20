plugins {
    kotlin("jvm")
    id("com.jakewharton.mosaic")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs += listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
    )
}

dependencies {
    implementation(project(":core:coroutines"))
}
