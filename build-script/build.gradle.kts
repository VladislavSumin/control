plugins {
    `kotlin-dsl`
}

dependencies {
    // TODO подождать пока эта фича появится в гредле
    // а пока костыль вот отсюда https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.gradlePlugins.kotlin.core)
    implementation(libs.gradlePlugins.kotlin.serialization)
    implementation(libs.gradlePlugins.checkUpdates)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins {
        create("EmptyPlugin") {
            id = "ru.vs.empty_plugin"
            implementationClass = "ru.vs.build_script.plugins.empty_plugin.EmptyPlugin"
        }
    }
}
