plugins {
  groovy
  id("com.github.ben-manes.versions")
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(8))
  }
}

dependencies {
  constraints {
    implementation("de.gesellix:docker-engine") {
      version {
        strictly("[2022-12-01T01-01-01,)")
      }
    }
    implementation("de.gesellix:docker-filesocket") {
      version {
        strictly("[2022-12-01T01-01-01,)")
      }
    }
    implementation("de.gesellix:docker-remote-api-model-1-41") {
      version {
        strictly("[2022-12-01T01-01-01,)")
      }
    }
    implementation("org.slf4j:slf4j-api") {
      version {
        strictly("[1.7,3)")
        prefer("2.0.6")
      }
    }
    listOf(
      "com.squareup.okhttp3:mockwebserver",
      "com.squareup.okhttp3:okhttp"
    ).onEach {
      implementation(it) {
        version {
          strictly("[4,5)")
          prefer("4.10.0")
        }
      }
    }
    implementation("com.squareup.okio:okio") {
      version {
        strictly("[3,4)")
      }
    }
    implementation("com.squareup.moshi:moshi") {
      version {
        strictly("[1.12.0,2)")
      }
    }
    implementation("com.squareup.moshi:moshi-kotlin") {
      version {
        strictly("[1.12.0,2)")
      }
    }
    listOf(
      "org.jetbrains.kotlin:kotlin-reflect",
      "org.jetbrains.kotlin:kotlin-stdlib",
      "org.jetbrains.kotlin:kotlin-stdlib-jdk7",
      "org.jetbrains.kotlin:kotlin-stdlib-jdk8",
      "org.jetbrains.kotlin:kotlin-stdlib-common",
      "org.jetbrains.kotlin:kotlin-test"
    ).onEach {
      implementation(it) {
        version {
          strictly("[1.5,1.9)")
          prefer("1.8.10")
        }
      }
    }
  }
  implementation(project(":client"))
  testImplementation("org.codehaus.groovy:groovy-json:[3,)")
  testImplementation("com.kohlschutter.junixsocket:junixsocket-core:[2.4,)")
  testImplementation("com.kohlschutter.junixsocket:junixsocket-common:[2.4,)")

  testImplementation("net.jodah:failsafe:2.4.4")
  testImplementation("org.apache.commons:commons-compress:1.22")

  testImplementation("org.slf4j:slf4j-api:[1.7,)")
  runtimeOnly("ch.qos.logback:logback-classic:[1.2,2)!!1.3.5")

  testImplementation("de.gesellix:docker-registry:2023-03-12T23-42-00")
  testImplementation("de.gesellix:testutil:[2022-12-01T01-01-01,)")
  testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
  testRuntimeOnly("net.bytebuddy:byte-buddy:1.14.1")
  testImplementation("org.apache.commons:commons-lang3:3.12.0")
  testRuntimeOnly("ch.qos.logback:logback-classic:[1.2,2)!!1.3.5")
}

tasks{
  withType<JavaCompile> {
    options.encoding = "UTF-8"
  }

  withType<Test> {
    useJUnitPlatform()
  }
}

tasks.check.get().shouldRunAfter(project(":client").tasks.check)
