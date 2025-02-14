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
    implementation(libs.okio) {
      version {
        strictly(libs.versions.okioVersionrange.get())
      }
    }
    listOf(libs.bundles.moshi).forEach {
      implementation(it) {
        version {
          strictly(libs.versions.moshiVersionrange.get())
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
  }
  implementation(project(":client"))
  testImplementation("org.codehaus.groovy:groovy-json:[3,4)")
//  testImplementation("org.apache.groovy:groovy-json:[4,)")
  testImplementation("com.kohlschutter.junixsocket:junixsocket-core:[2.4,)")
  testImplementation("com.kohlschutter.junixsocket:junixsocket-common:[2.4,)")

  testImplementation("net.jodah:failsafe:2.4.4")
  testImplementation("org.apache.commons:commons-compress:1.27.1")

  testImplementation("org.slf4j:slf4j-api:[1.7,)")
  runtimeOnly("ch.qos.logback:logback-classic:${libs.versions.logbackVersionrange.get()}!!${libs.versions.logback.get()}")

  testImplementation("de.gesellix:docker-registry:2025-01-18T21-41-00")
  testImplementation("de.gesellix:testutil:[2025-01-01T01-01-01,)")
  testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
//  testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")
  testRuntimeOnly("net.bytebuddy:byte-buddy:1.17.1")
  testImplementation("org.apache.commons:commons-lang3:3.17.0")
  testRuntimeOnly("ch.qos.logback:logback-classic:${libs.versions.logbackVersionrange.get()}!!${libs.versions.logback.get()}")
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
