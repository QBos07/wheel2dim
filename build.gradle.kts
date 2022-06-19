plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    //id("org.openjfx.javafxplugin") version "0.0.10"
    application
    //checkstyle
    id("org.jetbrains.dokka") version "1.6.10"
    //idea
}
group = "qbos.lejos.wheel2dim"
version = "1.0-SNAPSHOT"

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.ExperimentalUnsignedTypes")
    languageSettings.optIn("kotlin.RequiresOptIn")
}

//val tornadofxVersion: String by rootProject
val mainClassString: String by rootProject
val ev3MainClassString: String by rootProject
val ev3Lib: String by rootProject

repositories {
    mavenCentral()
    /*maven {
        url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
        content {
            includeGroup("org.jetbrains.pty4j")
        }
    }*/
}

application {
    mainClass.set(mainClassString)
}

val ev3Jar = File(ev3Lib)

dependencies {
    //implementation("no.tornado:tornadofx:$tornadofxVersion")
    //implementation("org.jetbrains.kotlinx:kotlinx-cli-jvm:0.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    /*implementation("org.jetbrains.pty4j:pty4j:0.12.7") {
        //exclude("org.jetbrains.pty4j", "purejavacomm")
    }*/
    //implementation("com.github.purejavacomm:purejavacomm:1.0.2.RELEASE")
    //implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation(files(ev3Jar))
    testImplementation(kotlin("test-testng", "1.6.21"))
    testImplementation("org.powermock:powermock-module-testng:2.0.9")
    testImplementation("org.powermock:powermock-api-mockito2:2.0.9")
    testImplementation("org.mockito:mockito-core:4.5.1")
    implementation(kotlin("reflect", "1.6.21"))
    //implementation(kotlin("script-runtime", "1.6.21"))
    implementation("com.charleskorn.kaml:kaml:0.44.0")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))

/*javafx {
    modules = listOf("javafx.controls", "javafx.graphics")
}*/

tasks.wrapper.get().gradleVersion = "7.4"
/*tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.ExperimentalUnsignedTypes"
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    //tasks.compileTestKotlin.get().kotlinOptions.jvmTarget = "1.8"
}*/
//tasks.withType<JavaCompile>().configureEach { options.release.set(8) }
//tasks.withType<ShadowJar>().configureEach { }

tasks.test {
    failFast = false
    useTestNG {
        /*includeGroups(
            "logger"
        )*/
    }
}
/*
idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
        jdkName = "temurin-1.8"
        languageLevel = IdeaLanguageLevel(8)
        targetVersion = "8"
        targetBytecodeVersion = JavaVersion.VERSION_1_8
        iml {
            whenMerged(Action<Module> {
                dependencies.forEach { (it as SingleEntryModuleLibrary).run {
                    //println("${this.libraryFile} : ${this.javadocFile} : ${this.sourceFile}")
                    if(this.libraryFile.path.contains("ev3classes.jar")) {
                        this.sourceFile = File(this.libraryFile.path.replace(".jar", "-src.zip"))
                    }
                }
                }
            })

            //singleEntryLibraries.forEach { (k, v) -> println("$k -> ${v.joinToString(separator = " ", transform = {it.path})}") }
        }

    }
    project {
        jdkName = "temurin-1.8"
        languageLevel = IdeaLanguageLevel(8)
        targetVersion = "8"
        targetBytecodeVersion = JavaVersion.VERSION_1_8
        vcs = "GitHub"
        //projectLibraries.forEach { println("${it.name} : ${it.type} : ${it.classes} : ${it.javadoc} : ${it.sources}") }
    }
}
*/

tasks.jar {
    archiveAppendix.set("")
    archiveVersion.set("")
}
tasks.shadowJar {
    archiveAppendix.set("")
    archiveVersion.set("")
    archiveClassifier.set("shadow")
}
tasks.register<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("ev3Jar") {
    minimize()
    archiveAppendix.set("")
    archiveVersion.set("")
    archiveClassifier.set("ev3")
    manifest.inheritFrom(project.tasks.jar.get().manifest)
    configurations = tasks.shadowJar.get().configurations
    exclude("lejos/**")
    from(sourceSets.main.get().output)
    manifest {
        attributes["Main-Class"] = ev3MainClassString
    }
    /*dependencies {
        exclude {
            it.moduleArtifacts.any { a -> a.file.absolutePath == ev3Jar.absolutePath }
        }
    }*/
}
tasks.register("deployJars") {
    dependsOn("ev3Jar", "shadowJar")
}