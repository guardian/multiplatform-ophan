Pod::Spec.new do |spec|
    spec.name                     = 'MultiplatformOphan'
    spec.version                  = '0.0.2'
    spec.homepage                 = 'https://github.com/guardian/multiplatform-ophan'
    spec.source                   = 'https://github.com/guardian/multiplatform-ophan/releases/download/v0.0.2/MultiplatformOphan.framework.zip'
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'A multiplatform client library for Ophan'

    spec.static_framework         = true
    spec.vendored_frameworks      = "#{spec.name}.framework"
    spec.libraries                = "c++"
    spec.module_name              = "#{spec.name}_umbrella"

            

    spec.pod_target_xcconfig = {
        'KOTLIN_TARGET[sdk=iphonesimulator*]' => 'ios_x64',
        'KOTLIN_TARGET[sdk=iphoneos*]' => 'ios_arm',
        'KOTLIN_TARGET[sdk=macosx*]' => 'macos_x64'
    }

    #spec.script_phases = [
    #    {
    #        :name => 'Build MultiplatformOphan',
    #        :execution_position => :before_compile,
    #        :shell_path => '/bin/sh',
    #        :script => <<-SCRIPT
    #            set -ev
    #            REPO_ROOT="$PODS_TARGET_SRCROOT"
    #            "$REPO_ROOT/gradlew" -p "$REPO_ROOT" ::syncFramework \
    #                -Pkotlin.native.cocoapods.target=$KOTLIN_TARGET \
    #                -Pkotlin.native.cocoapods.configuration=$CONFIGURATION \
    #                -Pkotlin.native.cocoapods.cflags="$OTHER_CFLAGS" \
    #                -Pkotlin.native.cocoapods.paths.headers="$HEADER_SEARCH_PATHS" \
    #                -Pkotlin.native.cocoapods.paths.frameworks="$FRAMEWORK_SEARCH_PATHS"
    #        SCRIPT
    #    }
    #]
end