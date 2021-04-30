import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask
import java.net.URI

plugins {
    kotlin("multiplatform") version "1.4.31"
    id("maven-publish")
    id("signing")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    val iosX64 = iosX64("ios")
    val iosArm64 = iosArm64("iosArm64")
    val iosArm32 = iosArm32("iosArm32")

    val frameworkName = "MultiplatformOphan"

    configure(listOf(iosX64, iosArm64, iosArm32)) {
        binaries.framework {
            baseName = frameworkName
        }
    }

    val coroutinesVersion = "1.4.3"
    val ktorVersion = "1.5.3"
    val klockVersion = "2.1.0"

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(kotlin("stdlib-common"))
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
                implementation(kotlin("stdlib"))
                implementation("io.ktor:ktor-client-android:$ktorVersion")
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
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm32Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }
        }
    }

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

group = "com.gu.kotlin"
version = "0.1.11"

afterEvaluate {
    publishing {
        publications
            .filterIsInstance<MavenPublication>()
            .forEach { publication ->
                //publication.groupId = "com.gu.kotlin"
                //publication.artifactId = "multiplatform-ophan"
                //publication.version = "0.1.11"
                publication.pom {
                    name.set("multiplatform-ophan")
                    description.set("A Kotlin Multiplatform client library for Ophan.")
                    url.set("https://github.com/guardian/multiplatform-ophan")
                    packaging = "jar" // TODO: why doesn't this show up in the POM?
                    licenses {
                        license {
                            name.set("The MIT License (MIT)")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/guardian/toolargetool.git")
                        developerConnection.set("scm:git:ssh://github.com/guardian/toolargetool.git")
                        url.set("https://github.com/guardian/toolargetool")

                    }
                    developers {
                        developer {
                            id.set("maxspencer")
                            name.set("Max Spencer")
                            email.set("max.spencer@guardian.co.uk")
                            url.set("https://github.com/maxspencer")
                            organization.set("The Guardian")
                            organizationUrl.set("https://theguardian.com")
                        }
                    }
                }
            }
        repositories {
            maven {
                name = "snapshot"
                url = URI.create("https://oss.sonatype.org/content/repositories/snapshots/")
                credentials {
                    username = properties["ossrhUsername"] as? String
                    password = properties["ossrhPassword"] as? String
                }
            }
            maven {
                name = "staging"
                url = URI.create("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = properties["ossrhUsername"] as? String
                    password = properties["ossrhPassword"] as? String
                }
            }
        }
    }
    signing {
        sign(publishing.publications)
    }
}

//apply(from = "publish.gradle")