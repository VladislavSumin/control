// TODO убрать когда апи станет стабильным
@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-script")

    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

rootProject.name = "control"

include(
    ":core:coroutines",
    ":core:crash-handler:api",
    ":core:crash-handler:impl",
    ":core:di",
    ":core:ktor-client",
    ":core:ktor-network",
    ":core:ktor-server",
    ":core:logging",
    ":core:logging-slf4j",
    ":core:mikrotik",
    ":core:sentry",
    ":core:serialization",
    ":core:utils-network",
)

include(
    ":feature:dns-sync:server",
    ":feature:proxy:server",
)

include(":server")

include(":samples:acme-server")