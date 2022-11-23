package com.iodigital.plugin

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

abstract class ConvertIconTask : Exec() {

    init {
        description = "Just a sample template task"

        // Don't forget to set the group here.
        // group = BasePlugin.BUILD_GROUP
    }

    @get:Input
    @get:Option(option = "text", description = "A message to be printed in the overlay")
    abstract val text: Property<String>

    @get:Input
    @get:Option(option = "color", description = "The color to be used for the overlay")
    abstract val color: Property<String>

    @get:Input
    @get:Option(option = "convertIOS", description = "Option to also generate the icons for iOS")
    abstract val convertIOS: Property<Boolean>

    @get:Input
    @get:Option(option = "inputFile", description = "The input file to be used for conversion")
    @get:Optional
    abstract val inputFile: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun action() {
        val prettyTag = "icon-processor:convertIcon"

        val t = text.orNull?.also { logger.lifecycle("$prettyTag text is: $it") }
        val absPath = project.projectDir.path + (inputFile.orNull ?: "")
        val f = absPath.also { logger.lifecycle("$prettyTag inputFilePath is: $absPath") }
        val o = outputDir.orNull?.asFile?.path?.also { logger.lifecycle("$prettyTag output is: $it") }
        val c = color.orNull?.also { logger.lifecycle("$prettyTag color is: $it") }
        val ios = convertIOS.orNull?.also { logger.lifecycle("$prettyTag convertIOS is: $it") } ?: false

        if (ios) {
            commandLine(TMP_PATH, "-t", t, "-f", f, "-o", o, "-c", c, "-a", "-i")
        } else {
            commandLine(TMP_PATH, "-t", t, "-f", f, "-o", o, "-c", c, "-a")
        }
    }
}
