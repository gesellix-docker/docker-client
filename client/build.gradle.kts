import java.text.SimpleDateFormat
import java.util.*

plugins {
  id("groovy")
  id("java-library")
  id("maven-publish")
  id("signing")
  id("com.github.ben-manes.versions")
  id("net.ossindex.audit")
  id("io.freefair.maven-central.validate-poms")
}

dependencies {
  constraints {
    implementation("de.gesellix:docker-engine") {
      version {
        strictly("[2025-01-01T01-01-01,)")
      }
    }
    implementation("de.gesellix:docker-filesocket") {
      version {
        strictly("[2025-01-01T01-01-01,)")
      }
    }
    implementation("de.gesellix:docker-remote-api-model-1-41") {
      version {
        strictly("[2025-01-01T01-01-01,)")
      }
    }
    implementation(libs.slf4j) {
      version {
        strictly(libs.versions.slf4jVersionrange.get())
        prefer(libs.versions.slf4j.get())
      }
    }
    listOf(libs.bundles.okhttp).forEach {
      implementation(it) {
        version {
          strictly(libs.versions.okhttpVersionrange.get())
          prefer(libs.versions.okhttp.get())
        }
      }
    }
    listOf(libs.bundles.okio).forEach {
      implementation(it) {
        version {
          strictly(libs.versions.okioVersionrange.get())
          prefer(libs.versions.okio.get())
        }
      }
    }
    listOf(libs.bundles.kotlin).forEach {
      implementation(it) {
        version {
          strictly(libs.versions.kotlinVersionrange.get())
          prefer(libs.versions.kotlin.get())
        }
      }
    }
    listOf(libs.bundles.groovy3).forEach {
      implementation(it) {
        version {
          strictly(libs.versions.groovy3Versionrange.get())
          prefer(libs.versions.groovy3.get())
        }
      }
    }
    listOf(libs.bundles.moshi).forEach {
      implementation(it) {
        version {
          strictly(libs.versions.moshiVersionrange.get())
          prefer(libs.versions.moshi.get())
        }
      }
    }
  }

  // TODO consider changing this from api to implementation.
  // The change would require to move api.core client classes like `ClientException` to another module.
  api("de.gesellix:docker-remote-api-client:2025-01-18T21-21-00")
  api("de.gesellix:docker-remote-api-model-1-41:2025-01-18T20-57-00")
  api("de.gesellix:docker-engine:2025-01-18T20-36-00")
  api("de.gesellix:docker-compose:2025-01-18T12-54-00")

  implementation(libs.groovy3)
  implementation(libs.groovy3json)

  api(libs.moshi)
  implementation("com.google.re2j:re2j:1.8")

  implementation(libs.slf4j)
  testImplementation("ch.qos.logback:logback-classic:${libs.versions.logbackVersionrange.get()}!!${libs.versions.logback.get()}")

  implementation(libs.okio)
  api(libs.okhttp)
  testImplementation("com.squareup.okhttp3:mockwebserver:${libs.versions.okhttp.get()}")

  implementation("org.apache.commons:commons-compress:1.27.1")

  implementation("org.bouncycastle:bcpkix-jdk18on:1.80")

  testImplementation("de.gesellix:testutil:[2025-01-01T01-01-01,)")

  testImplementation("org.junit.platform:junit-platform-launcher:1.11.4")
  testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
  testRuntimeOnly("net.bytebuddy:byte-buddy:1.17.1")
  testRuntimeOnly("org.objenesis:objenesis:3.4")
  testImplementation("io.github.joke:spock-mockable:2.3.0")

  testImplementation("org.apache.commons:commons-lang3:3.17.0")
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(8))
  }
}

tasks {
  withType<JavaCompile> {
    options.encoding = "UTF-8"
  }

  withType<Test> {
    useJUnitPlatform()

    // minimal way of providing a special environment variable for the EnvFileParserTest
    environment("A_KNOWN_VARIABLE", "my value")
  }
}

val javadocJar by tasks.registering(Jar::class) {
  dependsOn("classes")
  archiveClassifier.set("javadoc")
  from(tasks.javadoc)
}

val sourcesJar by tasks.registering(Jar::class) {
  dependsOn("classes")
  archiveClassifier.set("sources")
  from(sourceSets.main.get().allSource)
}

artifacts {
  add("archives", sourcesJar.get())
  add("archives", javadocJar.get())
}

fun findProperty(s: String) = project.findProperty(s) as String?

val isSnapshot = project.version == "unspecified"
val artifactVersion = if (!isSnapshot) project.version as String else SimpleDateFormat("yyyy-MM-dd\'T\'HH-mm-ss").format(Date())!!
val publicationName = "dockerClient"
publishing {
  repositories {
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/${property("github.package-registry.owner")}/${property("github.package-registry.repository")}")
      credentials {
        username = System.getenv("GITHUB_ACTOR") ?: findProperty("github.package-registry.username")
        password = System.getenv("GITHUB_TOKEN") ?: findProperty("github.package-registry.password")
      }
    }
  }
  publications {
    register(publicationName, MavenPublication::class) {
      pom {
        name.set("docker-client")
        description.set("A Docker client for the JVM written in Groovy")
        url.set("https://github.com/gesellix/docker-client")
        licenses {
          license {
            name.set("MIT")
            url.set("https://opensource.org/licenses/MIT")
          }
        }
        developers {
          developer {
            id.set("gesellix")
            name.set("Tobias Gesellchen")
            email.set("tobias@gesellix.de")
          }
        }
        scm {
          connection.set("scm:git:github.com/gesellix/docker-client.git")
          developerConnection.set("scm:git:ssh://github.com/gesellix/docker-client.git")
          url.set("https://github.com/gesellix/docker-client")
        }
      }
      artifactId = "docker-client"
      version = artifactVersion
      from(components["java"])
      artifact(sourcesJar.get())
      artifact(javadocJar.get())
    }
  }
}

signing {
  val signingKey: String? by project
  val signingPassword: String? by project
  useInMemoryPgpKeys(signingKey, signingPassword)
  sign(publishing.publications[publicationName])
}
