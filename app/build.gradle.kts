plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.composesample"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.composesample"
        minSdk = 24
        targetSdk = 35
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
}

dependencies {
    implementation (libs.androidx.material3.v120)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.kotlinx.coroutines.android)
    implementation(libs.androidx.activity.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.compose.v260)
    implementation (libs.material3)
    implementation (libs.kotlinx.coroutines.android.v171)
    implementation(platform(libs.androidx.compose.bom.v20230300))
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.androidx.compose.ui.ui.tooling.preview)
    implementation(libs.androidx.activity.compose.v1100)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.core.ktx)
    implementation (libs.androidx.ui.v140)
    implementation (libs.androidx.material3.v100)
    implementation (libs.androidx.runtime)
    implementation (libs.androidx.foundation)
    implementation (libs.androidx.activity.compose.v160)

    implementation (libs.kotlinx.serialization.json)
    implementation (libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.json.v163)
    implementation(libs.androidx.datastore.core.android)
}