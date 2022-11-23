plugins {
    java
    id("com.iodigital.iconprocessor")
}

icon {
    color.set("yellow")
    text.set("TEST")
    inputFile.set("/app/src/main/res/app-icon.png")
    outputDir.set(File(projectDir.path + "/app/src/main/res/"))
    convertIOS.set(false)
}
