buildscript {
    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
    }
}

plugins {
    groovy
    id("com.github.ben-manes.versions")
}

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
}

dependencies {
    compile(project(":client"))

    testCompile("net.jodah:failsafe:1.1.1")

    runtime("ch.qos.logback:logback-classic:1.2.3")

    testCompile("de.gesellix:testutil:2018-12-29T16-12-32")
    testCompile("org.spockframework:spock-core:1.2-groovy-2.5")
    testCompile("cglib:cglib-nodep:3.2.10")
    testCompile("org.apache.commons:commons-lang3:3.8.1")
    testCompile("joda-time:joda-time:2.10.1")
    testCompile("ch.qos.logback:logback-classic:1.2.3")
}
tasks.check.get().shouldRunAfter(project(":client").tasks.check)