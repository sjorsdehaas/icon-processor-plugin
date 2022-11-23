plugins {
    java
    id("com.iodigital.kotlin.gradle.template.plugin")
}

icon {
    color.set("yellow")
    text.set("TEST")
    inputFile.set(projectDir.path + "/app/src/main/res/app-icon.png")
    outputDir.set(File(projectDir.path + "/app/src/main/res/"))
    convertIOS.set(false)
}
