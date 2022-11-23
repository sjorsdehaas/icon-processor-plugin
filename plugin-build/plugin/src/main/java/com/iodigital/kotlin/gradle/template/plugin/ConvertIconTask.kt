package com.iodigital.kotlin.gradle.template.plugin

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
    abstract val outputFile: DirectoryProperty

    @TaskAction
    fun sampleAction() {
        val prettyTag = text.orNull?.let { "[$it]" } ?: ""

//        val watermark2 = javaClass.classLoader.getResource("watermark.sh")
//        val watermark = if (watermark2?.path?.contains("!") == true) {
//            val arch = watermark2.path?.substringBeforeLast("!")
//            project.resources.text.fromArchiveEntry(arch, "watermark.sh")
//        } else {
//            project.resources.text.fromFile(File(watermark2?.path ?: "watermark.sh"))
//        }
//
//        val tmpPath = "/tmp/watermark.sh"
//        val tmp = File(tmpPath)
//        val script = watermark.asString()
//        logger.lifecycle(script)
//        tmp.writeText(script)
//        tmp.setExecutable(true)

        val file = project.layout.projectDirectory.file(inputFile)
        val t = text.orNull?.also { logger.lifecycle("$prettyTag text is: $it") }
        val f = inputFile.orNull?.also { logger.lifecycle("$prettyTag inputFilePath is: $it") }
        val o = outputFile.orNull?.asFile?.path?.also { logger.lifecycle("$prettyTag output is: $it") }
        val c = color.orNull?.also { logger.lifecycle("$prettyTag color is: $it") }
        val ios = convertIOS.orNull?.also { logger.lifecycle("$prettyTag convertIOS is: $it") } ?: false
        logger.lifecycle("$prettyTag color is: ${color.orNull}")
        logger.lifecycle("$prettyTag inputFileStr is: ${inputFile.orNull }")
        logger.lifecycle("$prettyTag inputFileStr is: ${file.orNull?.asFile?.path }")
        logger.lifecycle("$prettyTag convertIOS is: ${convertIOS.orNull}")
        logger.lifecycle("$prettyTag outputFile is: ${outputFile.orNull?.asFile?.path}")

        if (ios) {
            commandLine(TMP_PATH, "-t", t, "-f", f, "-o", o, "-c", c, "-a", "-i")
        } else {
            commandLine(TMP_PATH, "-t", t, "-f", f, "-o", o, "-c", c, "-a")
        }
    }
}
