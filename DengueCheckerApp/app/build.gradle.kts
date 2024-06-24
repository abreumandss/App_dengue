plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 34  // Updated to SDK 34

    defaultConfig {
        applicationId = "com.example.denguecheckerapp"
        minSdk = 21
        targetSdk = 34  // Updated to SDK 34
    }

    namespace = "com.example.denguecheckerapp"

    buildTypes {
        getByName("release") {
            // release build type configurations
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("androidx.activity:activity-compose:1.4.0")

    // Corrected Compose dependencies
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.ui:ui-tooling:1.6.8")
    implementation("androidx.compose.foundation:foundation:1.6.8")
    implementation("androidx.compose.material:material:1.6.8")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}


kotlin {
    // Kotlin plugin configurations
    jvmToolchain {
        // Configure the JVM toolchain to use Java 17
        languageVersion = JavaLanguageVersion.of(17)
    }
}
