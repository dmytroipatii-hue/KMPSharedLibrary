import org.gradle.kotlin.dsl.support.kotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    // alias(libs.plugins.kotlin.android)
}

android {
    val androidNamespace = providers.gradleProperty("LIBRARY_IDENTIFIER").get()
    val compileSdkNumber = libs.versions.sdk.compile.get().toInt()
    val minSdkNumber = libs.versions.sdk.compile.get().toInt()

    namespace = androidNamespace
    compileSdk = compileSdkNumber

    defaultConfig {
        applicationId = androidNamespace
        minSdk = minSdkNumber
        targetSdk = compileSdkNumber
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.kotlin.multiplatform.gradle.plugin)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}