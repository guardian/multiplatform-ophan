# Multiplatform Ophan

A Kotlin Multiplatform client library for Ophan. Available for Android, iOS, and other Multiplatform projects.

## Adding as a dependency

The most recent version is `0.1.3`

### Android

Add the Guardian's kotlin maven repo to your project's repositories:

    # Project-level build.gradle
    
    allprojects {
        repositories {
            maven { url 'https://dl.bintray.com/guardian/kotlin' }
        }
    }
    
Add `multiplatform-ophan` as a dependency:

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

Add the Guardian's kotlin maven repo to your project's repositories:

    # build.gradle
    
    repositories {
        maven { url 'https://dl.bintray.com/guardian/kotlin' }
    }
    
Add `multiplatform-ophan` as a dependency:

    kotlin {
        sourceSets {
            commonMain {
                dependencies {
                    implementation 'com.gu.kotlin:multiplatform-ophan:<version>'
                }
            }
        }
    }
    
## Usage

Create an instance of `OphanDispatcher` and then use its `dispatchEvent` method to send `Event` instances to Ophan.

_TODO: Add more detail here!_

## Release process

_Important note: This will only work if you have appropriate Bintray credentials in a file named "bintray.properties"_

1. Use the "Replace in Path..." to update all version numbers,
2. Make a commit and tag it with `git tag -a v<version> -m "<message>"`
3. Upload to Bintray with `./gradlew bintrayUpload`
4. Publish in the Bintray web UI.
5. Publish the Podspec with updated version number with `pod repo push <repo name> MultiplatformOphan.podspec`
