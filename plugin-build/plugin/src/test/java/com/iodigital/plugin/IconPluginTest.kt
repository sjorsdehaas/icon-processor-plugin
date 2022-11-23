package com.iodigital.plugin

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.File

class IconPluginTest {

    @Test
    fun `plugin is applied correctly to the project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.iodigital.plugin")

        assert(project.tasks.getByName("convertIcon") is ConvertIconTask)
    }

    @Test
    fun `extension icon is created correctly`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.iodigital.plugin")

        assertNotNull(project.extensions.getByName("icon"))
    }

    @Test
    fun `parameters are passed correctly from extension to task`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.iodigital.plugin")
        val dir = File(project.projectDir.path)
        val path = project.projectDir.path + "input.tmp"
        (project.extensions.getByName("icon") as IconExtension).apply {
            color.set("yellow")
            convertIOS.set(true)
            inputFile.set(path)
            text.set("TEST")
            outputDir.set(dir)
        }

        val task = project.tasks.getByName("convertIcon") as ConvertIconTask

        assertEquals("yellow", task.color.get())
        assertEquals("TEST", task.text.get())
        assertEquals(true, task.convertIOS.get())
        assertEquals(path, task.inputFile.get())
        assertEquals(dir, task.outputDir.get().asFile)
    }
}
