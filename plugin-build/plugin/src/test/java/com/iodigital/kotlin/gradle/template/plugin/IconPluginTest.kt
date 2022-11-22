package com.iodigital.kotlin.gradle.template.plugin

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.File

class IconPluginTest {

    @Test
    fun `plugin is applied correctly to the project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.iodigital.kotlin.gradle.template.plugin")

        assert(project.tasks.getByName("convertIcon") is ConvertIconTask)
    }

    @Test
    fun `extension templateExampleConfig is created correctly`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.iodigital.kotlin.gradle.template.plugin")

        assertNotNull(project.extensions.getByName("icon"))
    }

    @Test
    fun `parameters are passed correctly from extension to task`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.iodigital.kotlin.gradle.template.plugin")
        val aFile = File(project.projectDir, ".tmp")
        (project.extensions.getByName("icon") as IconExtension).apply {
            tag.set("a-sample-tag")
            message.set("just-a-message")
            outputFile.set(aFile)
        }

        val task = project.tasks.getByName("convertIcon") as ConvertIconTask

        assertEquals("a-sample-tag", task.tag.get())
        assertEquals("just-a-message", task.message.get())
        assertEquals(aFile, task.outputFile.get().asFile)
    }
}
