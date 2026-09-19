@file:Suppress("UnstableApiUsage", "PropertyName")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.polyfrost.gradle.util.noServerRunConfigs

plugins {
    kotlin("jvm")
    id("org.polyfrost.multi-version")
    id("org.polyfrost.defaults.repo")
    id("org.polyfrost.defaults.java")
    id("org.polyfrost.defaults.loom")
    id("com.github.johnrengelman.shadow")
    id("net.kyori.blossom") version "1.3.2"
    java
}

val mod_name: String by project
val mod_version: String by project
val mod_id: String by project
val mod_archives_name: String by project

blossom {
    replaceToken("@VER@", mod_version)
    replaceToken("@NAME@", mod_name)
    replaceToken("@ID@", mod_id)
}

version = mod_version
group = "com.github.liwwyy"
base.archivesName.set("$mod_archives_name-$platform")

loom {
    noServerRunConfigs()

    if (project.platform.isLegacyForge) {
        runConfigs {
            "client" {
                programArgs("--tweakClass", "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
                property("mixin.debug.export", "true")
            }
        }
    }

    if (project.platform.isForge) {
        forge {
            mixinConfig("mixins.${mod_id}.json")
        }
    }

    mixin.defaultRefmapName.set("mixins.${mod_id}.refmap.json")
}

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}
val modShade: Configuration by configurations.creating {
    configurations.modImplementation.get().extendsFrom(this)
}

sourceSets {
    main {
        output.setResourcesDir(java.classesDirectory)
    }
}

repositories {
    maven("https://repo.polyfrost.org/releases")
}

dependencies {
    // OneConfig is supplied by the user's OneConfig installation at runtime.
    modCompileOnly("cc.polyfrost:oneconfig-$platform:0.2.2-alpha+")

    // Legacy Forge needs the OneConfig LaunchWrapper and Mixin 0.7.11 setup.
    if (project.platform.isLegacyForge) {
        compileOnly("org.spongepowered:mixin:0.7.11-SNAPSHOT")
        shade("cc.polyfrost:oneconfig-wrapper-launchwrapper:1.0.0-beta17")
    }
}

tasks {
    processResources {
        inputs.property("id", mod_id)
        inputs.property("name", mod_name)
        inputs.property("version", mod_version)
        inputs.property("mcVersionStr", project.platform.mcVersionStr)

        filesMatching(listOf("mcmod.info", "mixins.${mod_id}.json")) {
            expand(
                mapOf(
                    "id" to mod_id,
                    "name" to mod_name,
                    "version" to mod_version,
                    "mcVersionStr" to project.platform.mcVersionStr
                )
            )
        }
    }

    withType<Jar>().configureEach {
        exclude("mods.toml", "fabric.mod.json")
    }

    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("dev")
        configurations = listOf(shade, modShade)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveClassifier.set("")
    }

    jar {
        if (platform.isLegacyForge) {
            manifest.attributes += mapOf(
                "ModSide" to "CLIENT",
                "ForceLoadAsMod" to true,
                "TweakOrder" to "0",
                "MixinConfigs" to "mixins.${mod_id}.json",
                "TweakClass" to "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker"
            )
        }
        dependsOn(shadowJar)
        archiveClassifier.set("")
        enabled = false
    }
}
