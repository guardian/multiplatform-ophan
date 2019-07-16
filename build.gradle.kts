import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.3.40"
    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.4"
}

repositories {
    mavenCentral()
    jcenter()
}

kotlin {
    jvm()
    val iosSim = iosX64("ios")
    val ios64 = iosArm64("ios64")
    val ios32 = iosArm32("ios32")

    val frameworkName = "MultiplatformOphan"

    configure(listOf(iosSim, ios64, ios32)) {
        binaries.framework {
            baseName = frameworkName
        }
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.2.2")
                implementation("io.ktor:ktor-client-core:1.2.2")
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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.2")
                implementation("io.ktor:ktor-client-android:1.2.2")
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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.2.2")
                implementation("io.ktor:ktor-client-ios:1.2.2")
            }
        }
        val ios64Main by getting {
            dependsOn(iosMain)
        }
        val ios32Main by getting {
            dependsOn(iosMain)
        }
    }

    val debugFatFramework by tasks.creating(FatFrameworkTask::class) {
        baseName = frameworkName
        from(
            ios32.binaries.getFramework("debug"),
            ios64.binaries.getFramework("debug"),
            iosSim.binaries.getFramework("debug")
        )
        destinationDir = buildDir.resolve("fat-framework/debug")
        group = "Universal framework"
        description = "Builds a debug universal (fat) framework"
    }

    val releaseFatFramework by tasks.creating(FatFrameworkTask::class) {
        baseName = frameworkName
        from(
            ios32.binaries.getFramework("release"),
            ios64.binaries.getFramework("release"),
            iosSim.binaries.getFramework("release")
        )
        destinationDir = buildDir.resolve("fat-framework/release")
        group = "Universal framework"
        description = "Builds a release universal (fat) framework"
    }

    val zipDebugFatFramework by tasks.creating(Zip::class) {
        dependsOn(debugFatFramework)
        //archiveName = "MultiplatformOphan-iOS.zip"
        from(debugFatFramework)
        from("LICENSE.md")
    }

    val zipReleaseFatFramework by tasks.creating(Zip::class) {
        dependsOn(releaseFatFramework)
        //archiveName = "MultiplatformOphan-iOS.zip"
        from(releaseFatFramework)
        from("LICENSE.md")
    }

    publishing.publications.named<MavenPublication>("ios") {
        artifact(zipReleaseFatFramework)
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

apply(from = "publish.gradle")