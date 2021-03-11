import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact
import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask

plugins {
    //id("org.jetbrains.kotlin.multiplatform") version "1.3.41"
    kotlin("multiplatform") version "1.4.31"
    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.4"
}

repositories {
    mavenCentral()
    jcenter()
}

kotlin {
    jvm()
    js {
        nodejs()
        binaries.executable()
    }
    /*
    val iosX64 = iosX64("ios")
    val iosArm64 = iosArm64("iosArm64")
    val iosArm32 = iosArm32("iosArm32")

    val frameworkName = "MultiplatformOphan"

    configure(listOf(iosX64, iosArm64, iosArm32)) {
        binaries.framework {
            baseName = frameworkName
        }
    }
    */

    val coroutinesVersion = "1.4.3"
    val ktorVersion = "1.2.3"
    val klockVersion = "1.5.0"

    sourceSets {
        named("commonMain") {
            dependencies {
                //implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("com.soywiz.korlibs.klock:klock:$klockVersion")
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
                //implementation(kotlin("stdlib"))
                //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-RC")
                implementation("io.ktor:ktor-client-android:$ktorVersion")
            }
        }
        named("jvmTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        named("jsMain") {
            dependencies {
                //implementation(kotlin("stdlib"))
                //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-RC")
                //implementation("io.ktor:ktor-client-android:$ktorVersion")
            }
        }
        /*
        val iosMain by getting {
            dependencies {
                //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.0-RC")
                implementation("io.ktor:ktor-client-ios:1.2.3")
            }
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm32Main by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.0-RC")
                implementation("io.ktor:ktor-client-ios:1.2.3")
            }
        }
        */
    }

    /*
    val debugFatFramework by tasks.creating(FatFrameworkTask::class) {
        baseName = frameworkName
        from(
            iosArm32.binaries.getFramework("debug"),
            iosArm64.binaries.getFramework("debug"),
            iosX64.binaries.getFramework("debug")
        )
        destinationDir = buildDir.resolve("fat-framework/debug")
        group = "Universal framework"
        description = "Builds a debug universal (fat) framework"
    }

    val releaseFatFramework by tasks.creating(FatFrameworkTask::class) {
        baseName = frameworkName
        from(
            iosArm32.binaries.getFramework("release"),
            iosArm64.binaries.getFramework("release"),
            iosX64.binaries.getFramework("release")
        )
        destinationDir = buildDir.resolve("fat-framework/release")
        group = "Universal framework"
        description = "Builds a release universal (fat) framework"
    }

    val zipDebugFatFramework by tasks.creating(Zip::class) {
        dependsOn(debugFatFramework)
        from(debugFatFramework)
        from("LICENSE.md")
    }

    val zipReleaseFatFramework by tasks.creating(Zip::class) {
        dependsOn(releaseFatFramework)
        from(releaseFatFramework)
        from("LICENSE.md")
    }

    publishing.publications.named<MavenPublication>("ios") {
        artifact(zipReleaseFatFramework)
    }
    */
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
            "--path", "./thrift/",
            "./thrift/nativeapp.thrift"
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

//apply(from = "publish.gradle")