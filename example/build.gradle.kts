plugins {
    java
    id("com.iodigital.iconprocessor")
}

icon {
    color.set("black")
    text.set("PREPROD")
    inputFile.set("/app/src/main/res/drawable/app-icon.png")
    outputDir.set(File(projectDir.path + "/app/src/main/res/"))
    convertIOS.set(false)
}
