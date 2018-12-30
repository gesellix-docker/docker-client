import java.text.SimpleDateFormat
import java.util.*

rootProject.extra.set("artifactVersion", SimpleDateFormat("yyyy-MM-dd\'T\'HH-mm-ss").format(Date()))

buildscript {
    repositories {
        mavenLocal()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("com.github.ben-manes.versions") version "0.20.0"
    id("net.ossindex.audit") version "0.3.21"
    id("com.jfrog.bintray") version "1.8.4" apply false
}

val dependencyVersions = listOf(
        "com.squareup.okio:okio:2.1.0",
        "org.codehaus.groovy:groovy:2.5.4",
        "org.codehaus.groovy:groovy-json:2.5.4",
        "org.jetbrains.kotlin:kotlin-reflect:1.3.11",
        "org.jetbrains.kotlin:kotlin-stdlib:1.3.11",
        "org.jetbrains.kotlin:kotlin-stdlib-common:1.3.11"
)

subprojects {
    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()
            force(dependencyVersions)
        }
    }
}

tasks {
    register<Wrapper>("updateWrapper") {
        gradleVersion = "5.0"
        distributionType = Wrapper.DistributionType.ALL
    }
}

project.apply("debug.gradle.kts")