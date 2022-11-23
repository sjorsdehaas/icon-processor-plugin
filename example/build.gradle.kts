plugins {
    java
    id("com.iodigital.kotlin.gradle.template.plugin")
}

icon {
    color.set("yellow")
    text.set("TEST")
    inputFile.set(projectDir.path + "/app/src/main/res/app-icon.png")
    outputFile.set(File(projectDir.path + "/app/src/main/res/"))
    convertIOS.set(false)
}
