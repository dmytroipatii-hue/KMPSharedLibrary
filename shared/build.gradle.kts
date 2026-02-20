import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.gradle.api.credentials.PasswordCredentials
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.vanniktech.mavenPublish)
}


private fun Project.configureKMPLibrary() {
    extensions.configure(KotlinMultiplatformExtension::class.java) {

        // Explicit API mode
        // This mode, the compiler performs additional checks that help make the library's API clearer and more consistent
        // See: https://kotlinlang.org/docs/whatsnew14.html#explicit-api-mode-for-library-authors

        explicitApi()
        // explicitApiWarning()

    }
}
private fun Project.configureAndroidLibrary() {
    extensions.configure(KotlinMultiplatformExtension::class.java){

        val androidNamespace = providers.gradleProperty("ANDROID_NAMESPACE").get()
        val compileSdkNumber = providers.gradleProperty("COMPILE_SDK").map(String::toInt).get()
        val minSdkNumber = providers.gradleProperty("MIN_SDK").map(String::toInt).get()

        // Android target configuration based on the official Kotlin Multiplatform documentation.
        // Documentation reference: https://kotlinlang.org/docs/multiplatform/multiplatform-publish-lib-setup.html#publish-an-android-library

        androidLibrary{
            namespace = androidNamespace
            compileSdk = compileSdkNumber
            minSdk = minSdkNumber

            compilations.configureEach {
                compileTaskProvider.configure{
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_11)
                    }
                }
            }

            // Enables Java compilation support.
            withJava()
            
            withHostTestBuilder {
            }

            withDeviceTestBuilder {
                sourceSetTreeName = "test"
            }.configure {
                instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
        }
    }
}
private fun Project.configureIOSLibrary() {
    extensions.configure(KotlinMultiplatformExtension::class.java) {

        val frameworkName = providers.gradleProperty("FRAMEWORK_NAME")
            .orElse("KMPSharedLibrary")
            .get()

        val bundleId = providers.gradleProperty("IOS_BUNDLE_ID").get()

        // For iOS targets, this is also where you should
        // configure native binary output. For more information, see:
        // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

        val iOSTargets = listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        )

        // A step-by-step guide on how to include this library in an XCode
        // project can be found here:
        // https://developer.android.com/kotlin/multiplatform/migrate
        val xcFramework = XCFramework(xcFrameworkName = frameworkName)

        iOSTargets.forEach { target ->
            target.binaries.framework {
                baseName = frameworkName
                binaryOption("bundleId", bundleId)

                xcFramework.add(this)
                debuggable = false
                isStatic = true
            }

        }
    }
}


kotlin {
    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    configureKMPLibrary()

    configureAndroidLibrary()

    configureIOSLibrary()

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                // Add KMP dependencies here
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.junit)
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}


group = providers.gradleProperty("GROUP_ID").get()
version = providers.gradleProperty("VERSION_NAME")
    .orElse("LOCAL_VERSION_NAME")
    .get()

mavenPublishing {

    publishing {
        repositories {
            maven {
                name = providers.gradleProperty("MAVEN_REPOSITORY_NAME").get()
                url = uri(providers.gradleProperty("MAVEN_REPOSITORY_URL").get())
                credentials(PasswordCredentials::class)
            }
        }
    }

    // Define coordinates for the published artifact
    coordinates(
        artifactId = providers.gradleProperty("ARTIFACT_ID").get(),
    )

}

