package com.iodigital.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

const val EXTENSION_NAME = "icon"
const val TASK_NAME = "convertIcon"
const val TMP_PATH = "/tmp/process-icon.sh"

abstract class IconPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Add the 'template' extension object
        val extension = project.extensions.create(EXTENSION_NAME, IconExtension::class.java, project)

        loadScript(project)

        // Add a task that uses configuration from the extension object
        project.tasks.register(TASK_NAME, ConvertIconTask::class.java) {
            it.inputFile.set(extension.inputFile)
            it.convertIOS.set(extension.convertIOS)
            it.color.set(extension.color)
            it.text.set(extension.text)
            it.outputDir.set(extension.outputDir)

            it.action()
        }
    }

    private fun loadScript(project: Project) {
        val res = javaClass.classLoader.getResource("watermark.sh")
        val watermarkScript = if (res?.path?.contains("!") == true) {
            val arch = res.path?.substringBeforeLast("!") ?: ""
            project.resources.text.fromArchiveEntry(arch, "watermark.sh")
        } else {
            project.resources.text.fromFile(File(res?.path ?: "watermark.sh"))
        }

        val tmp = File(TMP_PATH)
        val script = watermarkScript.asString()
        tmp.writeText(script)
        tmp.setExecutable(true)
    }
}
