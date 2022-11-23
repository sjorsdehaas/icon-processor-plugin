
# Android Icon Processor Gradle Plugin 🐘

[![Gradle Plugin Repository:](https://plugins.gradle.org/plugin/com.iodigital.plugin.iconpressor)](https://github.com/cortinico/kotlin-gradle-plugin-template/generate) [![Pre Merge Checks](https://github.com/cortinico/kotlin-gradle-plugin-template/workflows/Pre%20Merge%20Checks/badge.svg)](https://github.com/cortinico/kotlin-gradle-plugin-template/actions?query=workflow%3A%22Pre+Merge+Checks%22)  [![License](https://img.shields.io/github/license/cortinico/kotlin-android-template.svg)](LICENSE) ![Language](https://img.shields.io/github/languages/top/cortinico/kotlin-android-template?color=blue&logo=kotlin)

A Gradle plugin that adds a task for converting your icon launcher to the correct resolutions.
Can be ran as a standalone task or included as a build step.

## How to use 👣

### Add the plugin to your project
Apply the plugin to your build.gradle:

```
plugins {
    id "com.iodigital.iconprocessor"
}
```
or build.gradle.kts
```
plugins {
    id ("com.iodigital.iconprocessor")
}
```
Add them as a task to your build script:

build.gradle:

```
task("convertIcon").dependsOn("check")
task("convertIcon").finalizedBy("check")
```

build.gradle.kts:
```
tasks.findByName("convertIcon")?.finalizedBy("check")
tasks.findByName("convertIcon")?.dependsOn("check")
```

###Configuration:
When running the plugin as part of your build, or when wrapping it in a task of your own, you can provide the configuration with the ```icon``` block.
build.gradle
```
icon {
    color "red"
    text "debug"
    convertIOS false
    outputDir File(projectDir.path + "/app/src/main/res")
    inputFile "/app/src/main/res/app-icon.png"
}
```
build.gradle.kts
```
icon {
    color.set("yellow")
    text.set("TEST")
    inputFile.set(projectDir.path + "/app/src/main/res/app-icon.png")
    outputDir.set(File(projectDir.path + "/app/src/main/res/"))
    convertIOS.set(false)
}
```
All these properties are optional.
Defaults:
```
icon {
    color "red"
    text "debug"
    convertIOS false
    outputDir File(projectDir.path + "/app/src/main/res")
    inputFile "/app/src/main/res/app-icon.png"
}
```
You can also invoke the plugin task from CLI or wrap it in your own task.
Available task is ```convertIcon```
```
gradlew convertIcon --text="UAT" --color="green"
```

Wrapper task:
build.gradle
```
tasks {
    register("wrapperTask") {
        icon {
            color "red"
            text "debug"
            convertIOS false
            outputDir File(projectDir.path + "/app/src/main/res")
            inputFile "/app/src/main/res/app-icon.png"
        }
        doLast { tasks.findByPath("convertIcon") }
    }
}
```
build.gradle.kts
```
tasks.register("wrapperTask") {
    icon {
        color.set("red")
        text.set("Wrapped")
        inputFile.set("/app/src/main/res/app-icon.png")
        outputDir.set(File(projectDir.path + "/app/src/main/res/"))
        convertIOS.set(false)
    }
    doLast {
        tasks.findByName("convertIcon")
    }
}
```
Execute:
```gradlew wrapperTask```

## Features 🎨

- **100% Kotlin-only template**.
- Plugin build setup with **composite build**.
- 100% Gradle Kotlin DSL setup.
- Dependency versions managed via Gradle Versions Catalog (`libs.versions.toml`).
- CI Setup with GitHub Actions.
- Kotlin Static Analysis via `ktlint` and `detekt`.
- Publishing-ready to Gradle Portal.
- Issues Template (bug report + feature request)
- Pull Request Template.

## Composite Build 📦

This template is using a [Gradle composite build](https://docs.gradle.org/current/userguide/composite_builds.html) to build, test and publish the plugin. This means that you don't need to run Gradle twice to test the changes on your Gradle plugin (no more `publishToMavenLocal` tricks or so).

The included build is inside the [plugin-build](plugin-build) folder.

### `preMerge` task

A `preMerge` task on the top level build is already provided in the template. This allows you to run all the `check` tasks both in the top level and in the included build.

You can easily invoke it with:

```
./gradlew preMerge
```

If you need to invoke a task inside the included build with:

```
./gradlew -p plugin-build <task-name>
```


### Dependency substitution

Please note that the project relies on module name/group in order for [dependency substitution](https://docs.gradle.org/current/userguide/resolution_rules.html#sec:dependency_substitution_rules) to work properly. If you change only the plugin ID everything will work as expected. If you change module name/group, things might break and you probably have to specify a [substitution rule](https://docs.gradle.org/current/userguide/resolution_rules.html#sub:project_to_module_substitution).


## Publishing 🚀

This template is ready to let you publish to [Gradle Portal](https://plugins.gradle.org/).

The [![Publish Plugin to Portal](https://github.com/cortinico/kotlin-gradle-plugin-template/workflows/Publish%20Plugin%20to%20Portal/badge.svg?branch=1.0.0)](https://github.com/cortinico/kotlin-gradle-plugin-template/actions?query=workflow%3A%22Publish+Plugin+to+Portal%22) Github Action will take care of the publishing whenever you **push a tag**.

Please note that you need to configure two secrets: `GRADLE_PUBLISH_KEY` and `GRADLE_PUBLISH_SECRET` with the credetials you can get from your profile on the Gradle Portal.

## 100% Kotlin 🅺

This plugin is designed to use Kotlin everywhere. The build files are written using [**Gradle Kotlin DSL**](https://docs.gradle.org/current/userguide/kotlin_dsl.html) as well as the [Plugin DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block) to setup the build.

Dependencies are centralized inside the [libs.versions.toml](gradle/libs.versions.toml).

## Static Analysis 🔍

This template is using [**ktlint**](https://github.com/pinterest/ktlint) with the [ktlint-gradle](https://github.com/jlleitschuh/ktlint-gradle) plugin to format your code. To reformat all the source code as well as the buildscript you can run the `ktlintFormat` gradle task.

This template is also using [**detekt**](https://github.com/arturbosch/detekt) to analyze the source code, with the configuration that is stored in the [detekt.yml](config/detekt/detekt.yml) file (the file has been generated with the `detektGenerateConfig` task).

## CI ⚙️

This plugin is using [**GitHub Actions**](https://github.com/cortinico/kotlin-android-template/actions) as CI.

There are currently the following workflows available:
- [Validate Gradle Wrapper](.github/workflows/gradle-wrapper-validation.yml) - Will check that the gradle wrapper has a valid checksum
- [Pre Merge Checks](.github/workflows/pre-merge.yaml) - Will run the `preMerge` tasks as well as trying to run the Gradle plugin.
- [Publish to Plugin Portal](.github/workflows/publish-plugin.yaml) - Will run the `publishPlugin` task when pushing a new tag.

## Contributing 🤝

Feel free to open a issue or submit a pull request for any bugs/improvements.

## Known issues

From the preMerge CI actions it seems to be that there is an issue regarding windows OS.
I have not tested this on a windows device and all attempts to solve it have been futile so far.
Feel free to have a go at it.

## License 📄

This template is licensed under the MIT License - see the [License](License) file for details.
Please note that the generated template is offering to start with a MIT license but you can change it to whatever you wish, as long as you attribute under the MIT terms that you're using the template.
