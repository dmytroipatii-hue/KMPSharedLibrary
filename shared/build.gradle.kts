import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.vanniktech.mavenPublish)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "io.github.kmpsharedlibrary"
        compileSdk = 36
        minSdk = 24


        // Enables Java compilation support.
        // This improves build times when Java compilation is not needed
        withJava()


        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)

        }


        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    /* ************** iOS targets configurations ************** */
    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate

    val spmFrameworkName = "KMPSharedLibrary"

    val iOSTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    val xcFramework = XCFramework(xcFrameworkName = spmFrameworkName)

    iOSTargets.forEach { target ->
        target.binaries.framework {
            baseName = spmFrameworkName
            xcFramework.add(this)
            isStatic = true
        }

    }


    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
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

group = "io.github"
version = "1.0.0"

publishing {
    repositories {

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/dmytroipatii-hue/KMPSharedLibrary")
            credentials {
                username = providers.gradleProperty("githubPackagesUsername")
                    .orElse(providers.environmentVariable("USER_NAME"))
                    .get()

                password = providers.gradleProperty("githubPackagesPassword")
                    .orElse(providers.environmentVariable("DEV_ACCESS_TOKEN"))
                    .get()
            }
        }
    }
}

mavenPublishing {
    // Define coordinates for the published artifact
    coordinates(
        artifactId = "kmpsharedlibrary",
    )



    // Configure POM metadata for the published artifact
    pom {
        name.set("KMP Shared Library")
        description.set("Sample Kotlin Multiplatform Library")
        url.set("https://github.com/dmytroipatii-hue/KMPSharedLibrary")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("dmytroipatii-hue")
                name.set("Dmytro Ipatii")
            }
        }

        scm {
            url.set("https://github.com/dmytroipatii-hue/KMPSharedLibrary")
            connection.set("scm:git:git://github.com/dmytroipatii-hue/KMPSharedLibrary.git")
            developerConnection.set("scm:git:ssh://git@github.com/dmytroipatii-hue/KMPSharedLibrary.git")
        }

    }
}

