plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    val androidNamespace = providers.gradleProperty("ANDROID_NAMESPACE").get()
    val compileSdkNumber = providers.gradleProperty("COMPILE_SDK").map(String::toInt).get()
    val minSdkNumber = providers.gradleProperty("MIN_SDK").map(String::toInt).get()

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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}