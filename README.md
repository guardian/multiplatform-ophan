# Multiplatform Ophan

A Kotlin Multiplatform client library for Ophan. Available for Android, iOS, and other Multiplatform projects.

## Adding as a dependency

The most recent version is `0.0.4`

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

TODO: Add more detail here!
