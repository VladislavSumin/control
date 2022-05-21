package ru.vs.build_script.plugins.empty_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * При подключении модуля через:
 * pluginManagement {
 *     includeBuild("build-script")
 * }
 * гредл не импортирует class path компонента, если не подключен ни один плагин из этого компонента
 * данный плагин можно использовать для подключения компонента в class path, если не планируется использование
 * других плагинов из этого модуля
 */
class EmptyPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // no action
    }
}
