# Multiplatform Ophan

A Kotlin Multiplatform client library for Ophan. Available for Android, iOS, and other Multiplatform projects.

## Adding as a dependency

The most recent version is `0.2.0`
Snapshot version: `0.2.1-SNAPSHOT`

### Android
    
Add `multiplatform-ophan` as a dependency (available in Maven Central):

    # Module-level build.gradle
    
    dependencies {
         implementation 'com.gu.kotlin:multiplatform-ophan:<version>'
    }


    
### iOS

Add the Guardian's private spec repo:

    pod repo add guardian git@github.com:guardian/specs.git
    
Add the `MultiplatformOphan` pod to your Podfile:

    # Podfile
    
    source 'git://github.com/guardian/specs'
    target 'Example' do
      pod 'MultiplatformOphan'
    end
    
Here is [an example](https://github.com/guardian/ios-live/commit/465d98846b37a3d2943d6c516d42c6b296e3fb7e) of this applied to the Guardian's iOS app.

### Multiplatform

Add `multiplatform-ophan` as a dependency (available in Maven Central):

    kotlin {
        sourceSets {
            commonMain {
                dependencies {
                    implementation 'com.gu.kotlin:multiplatform-ophan:<version>'
                }
            }
        }
    }

### Scala

Add this to your project's sbt config (available in Maven Central):

    # build.sbt

    libraryDependencies += "com.gu.kotlin" % "multiplatform-ophan-jvm" % "<version>"
    
## Usage

Create an instance of `OphanDispatcher` and then use its `dispatchEvent` method to send `Event` instances to Ophan.

_TODO: Add more detail here!_

## Release process

GitHub Actions are used to publish this library. The credentials required for signing and publishing builds are stored
as repository secrets.

* The `Publish snapshot` workflow publishes a snapshot to Sonatype's
  [snapshot repo](https://oss.sonatype.org/content/repositories/snapshots/com/gu/kotlin/) automatically each time
  `main` is changed.
* The `Publish release` workflow is invoked manually and publishes to Sonatype's staging repo, from which it can be
  pushed into [Maven Central](https://search.maven.org/search?q=com.gu.kotlin).

After making a release (with the `Publish release` workflow) please follow these additional steps:

1. Tag the commit which was released with `git tag -a v<version> -m "<message>"
2. Use find-replace to update all version numbers in the repo:
   a. in [README.md]:
      i. the "most recent version" should be the `<version>` that was just released, and
      ii. the "snapshot version" should be the `<new-version>` that will succeed `<version>`
   b. and everywhere else all version numbers should be changed to `<new-version>`.
3. Commit and push and a `<new-version>-SNAPSHOT` will be published automatically. 