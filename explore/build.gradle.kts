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
    implementation(libs.slf4j) {
      version {
        strictly(libs.versions.slf4jVersionrange.get())
        prefer(libs.versions.slf4j.get())
      }
    }
    implementation(libs.okio) {
      version {
        strictly(libs.versions.okioVersionrange.get())
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
    listOf(libs.bundles.groovy4).forEach {
      implementation(it) {
        version {
          strictly(libs.versions.groovy4Versionrange.get())
          prefer(libs.versions.groovy4.get())
        }
      }
    }
  }
//  implementation(project(":client"))
  implementation(project(":client-groovy4"))
//  implementation(libs.groovy3)
  implementation(libs.groovy4)
  testImplementation("org.apache.commons:commons-compress:1.27.1")

  implementation(libs.slf4j)
  runtimeOnly("ch.qos.logback:logback-classic:${libs.versions.logbackVersionrange.get()}!!${libs.versions.logback.get()}")

//  testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
  testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")
  testRuntimeOnly("net.bytebuddy:byte-buddy:1.17.1")
  testRuntimeOnly("ch.qos.logback:logback-classic:${libs.versions.logbackVersionrange.get()}!!${libs.versions.logback.get()}")
}

tasks {
  withType<JavaCompile> {
    options.encoding = "UTF-8"
  }
  withType<Test> {
    useJUnitPlatform()
  }
}
