package com.iodigital.plugin

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import javax.inject.Inject

const val DEFAULT_OUTPUT_FILE = "app/src/main/res"
const val DEFAULT_INPUT_FILE = "app/src/main/res/ic_launcher.png"

@Suppress("UnnecessaryAbstractClass")
abstract class IconExtension @Inject constructor(private val project: Project) {

    private val objects = project.objects

    // Example of a property that is mandatory. The task will
    // fail if this property is not set as is annotated with @Optional.
    @Optional
    val text: Property<String> = objects.property(String::class.java).convention("debug")

    @Optional
    val inputFile: Property<String> = objects.property(String::class.java).convention(DEFAULT_INPUT_FILE)

    @Optional
    val color: Property<String> = objects.property(String::class.java).convention("red")

    @Optional
    val convertIOS: Property<Boolean> = objects.property(Boolean::class.java).convention(false)

    @Optional
    @OutputDirectory
    val outputDir: DirectoryProperty = objects.directoryProperty().convention(
        project.layout.projectDirectory.dir(DEFAULT_OUTPUT_FILE)
    )
}
