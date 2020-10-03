import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.internal.plugins.DslObject

buildscript {
    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
    }
}

project.extra.set("bintrayDryRun", false)

plugins {
    groovy
    `java-library`
    maven
    `maven-publish`
    id("com.github.ben-manes.versions")
    id("net.ossindex.audit")
    id("com.jfrog.bintray")
    id("io.freefair.github.package-registry-maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
}

dependencies {
    constraints {
        implementation("org.slf4j:slf4j-api") {
            version {
                strictly("[1.7,1.8)")
                prefer("1.7.30")
            }
        }
        implementation("com.squareup.okhttp3:mockwebserver") {
            version {
                strictly("[4,5)")
                prefer("4.9.0")
            }
        }
        api("com.squareup.okhttp3:okhttp") {
            version {
                strictly("[4,5)")
                prefer("4.9.0")
            }
        }
        implementation("com.squareup.okio:okio") {
            version {
                strictly("[2.5,3)")
                prefer("2.8.0")
            }
        }
        listOf("org.jetbrains.kotlin:kotlin-reflect",
                "org.jetbrains.kotlin:kotlin-stdlib",
                "org.jetbrains.kotlin:kotlin-stdlib-jdk8",
                "org.jetbrains.kotlin:kotlin-stdlib-common",
                "org.jetbrains.kotlin:kotlin-test").onEach {
            implementation(it) {
                version {
                    strictly("[1.3,1.5)")
                    prefer("1.3.72")
                }
            }
        }
        listOf("org.codehaus.groovy:groovy",
                "org.codehaus.groovy:groovy-json").onEach {
            implementation(it) {
                version {
                    strictly("[2.5,3)")
                    prefer("2.5.13")
                }
            }
        }
    }

    api("de.gesellix:docker-engine:2020-10-03T11-21-27")
    api("de.gesellix:docker-compose:2020-10-03T10-26-14")

    implementation("org.codehaus.groovy:groovy:[2.5,)")
    implementation("org.codehaus.groovy:groovy-json:[2.5,)")

    api("com.squareup.moshi:moshi:[1.9,2)")
    implementation("com.google.re2j:re2j:1.4")

    implementation("org.slf4j:slf4j-api:[1.7,)")
    //implementation("org.slf4j:jul-to-slf4j:1.7.30")
    testImplementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("com.squareup.okio:okio:[2,)")
    api("com.squareup.okhttp3:okhttp:[4,5)")
    testImplementation("com.squareup.okhttp3:mockwebserver:[4,5)")

    implementation("org.apache.commons:commons-compress:1.20")

    implementation("org.bouncycastle:bcpkix-jdk15on:1.66")

    testImplementation("de.gesellix:testutil:[2020-10-03T10-08-28,)")

    testImplementation("org.spockframework:spock-core:1.3-groovy-2.5")
    testRuntimeOnly("cglib:cglib-nodep:3.3.0")

    testImplementation("org.apache.commons:commons-lang3:3.11")
}

tasks {
    withType(Test::class.java) {
        useJUnit()

        // minimal way of providing a special environment variable for the EnvFileParserTest
        environment("A_KNOWN_VARIABLE", "my value")
    }

    bintrayUpload {
        dependsOn("build")
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    dependsOn("classes")
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

artifacts {
    add("archives", sourcesJar.get())
}

tasks.install {
    DslObject(repositories)
            .convention
            .getPlugin<MavenRepositoryHandlerConvention>()
            .mavenInstaller {
                pom {
                    groupId = "de.gesellix"
                    artifactId = "docker-client"
                    version = "local"
                }
            }
}

val publicationName = "dockerClient"
publishing {
    publications {
        register(publicationName, MavenPublication::class) {
            groupId = "de.gesellix"
            artifactId = "docker-client"
            version = rootProject.extra["artifactVersion"] as String
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}

fun findProperty(s: String) = project.findProperty(s) as String?

bintray {
    user = System.getenv()["BINTRAY_USER"] ?: findProperty("bintray.user")
    key = System.getenv()["BINTRAY_API_KEY"] ?: findProperty("bintray.key")
    setPublications(publicationName)
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "docker-utils"
        name = "docker-client"
        desc = "A Docker client for the JVM written in Groovy"
        setLicenses("Apache-2.0")
        setLabels("docker", "engine api", "remote api", "client", "java", "groovy")
        version.name = rootProject.extra["artifactVersion"] as String
        vcsUrl = "https://github.com/gesellix/docker-client.git"
    })
    dryRun = project.extra["bintrayDryRun"] as Boolean
}
