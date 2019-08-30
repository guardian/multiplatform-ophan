import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.3.41"
    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.4"
}

repositories {
    mavenCentral()
    jcenter()
}

kotlin {
    jvm()

    // This is for iPhone emulator
    // Switch here to iosArm64 (or iosArm32) to build library for iPhone device
    val ios64 = iosX64("ios") {
        binaries {
            framework {
                baseName = "MultiplatformOphan"
            }
        }
    }

    val iosArm64 = iosArm64("iosArm64") {
        binaries {
            framework {
                baseName = "MultiplatformOphan"
            }
        }
    }

    val iosArm32 = iosArm32("iosArm32") {
        binaries {
            framework {
                baseName = "MultiplatformOphan"
            }
        }
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.0-RC")
                implementation("io.ktor:ktor-client-core:1.2.3")
                implementation("com.soywiz.korlibs.klock:klock:1.5.0")
            }
        }
        named("commonTest") {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        named("jvmMain") {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-RC")
                implementation("io.ktor:ktor-client-android:1.2.3")
            }
        }
        named("jvmTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.0-RC")
                implementation("io.ktor:ktor-client-ios:1.2.3")
            }
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm32Main by getting {
            //dependsOn(iosMain)
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.0-RC")
                implementation("io.ktor:ktor-client-ios:1.2.3")
            }
        }
    }

    val linkDebugFrameworkIos by tasks.getting

    val zipIosFramework by tasks.creating(Zip::class) {
        dependsOn(linkDebugFrameworkIos)
        archiveName = "MultiplatformOphan-iOS.zip"
        from(ios64.binaries.getFramework("debug").outputDirectory)
        from("LICENSE.md")
    }

    publishing.publications.named<MavenPublication>("ios") {
        artifact(zipIosFramework)
    }
}

tasks.register<Exec>("generateThriftModels") {
    executable = "java"
    args(
            "-jar", "thrifty-compiler.jar",
            "--lang", "kotlin",
            "--omit-generated-annotations",
            "--list-type=kotlin.collections.ArrayList",
            "--set-type=kotlin.collections.LinkedHashSet",
            "--out", "src/commonMain/kotlin",
            "--path", "../android-news-app/ophan/event-model/src/main/thrift/",
            "../android-news-app/ophan/event-model/src/main/thrift/nativeapp.thrift"
    )
}

// This workaround makes bintrayUpload include the important .module files
// https://github.com/bintray/gradle-bintray-plugin/issues/229#issuecomment-473123891
tasks.withType<BintrayUploadTask> {
    doFirst {
        publishing.publications
            .filterIsInstance<MavenPublication>()
            .forEach { publication ->
                val moduleFile = buildDir.resolve("publications/${publication.name}/module.json")
                if (moduleFile.exists()) {
                    publication.artifact(object : FileBasedMavenArtifact(moduleFile) {
                        override fun getDefaultExtension() = "module"
                    })
                }
            }
    }
}

apply(from = "publish.gradle")