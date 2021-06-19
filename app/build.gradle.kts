plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.xujichang.jetnews"
        minSdk = 21
        targetSdk = 30
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0-beta08"
    }
}

dependencies {
    val activity_version = "1.3.0-beta01"
    val compose_version = "1.0.0-beta08"
    val accompanist_version = "0.11.1"
    //coroutions
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    implementation("androidx.core:core-ktx:1.5.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.3.0")
    //compose
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.ui:ui-tooling:$compose_version")
    implementation("androidx.compose.runtime:runtime:$compose_version")
    implementation("androidx.compose.runtime:runtime-livedata:$compose_version")
    implementation("androidx.compose.runtime:runtime-rxjava2:$compose_version")
    implementation("androidx.compose.foundation:foundation:$compose_version")
    implementation("androidx.compose.foundation:foundation-layout:$compose_version")
    implementation("androidx.compose.compiler:compiler:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.material:material-icons-extended:$compose_version")
    implementation("androidx.compose.animation:animation:$compose_version")
    //activity
    implementation("androidx.activity:activity-compose:$activity_version")
    //accompanist
    implementation("com.google.accompanist:accompanist-swiperefresh:$accompanist_version")
    implementation("com.google.accompanist:accompanist-insets:$accompanist_version")
    implementation("com.google.accompanist:accompanist-insets-ui:$accompanist_version")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanist_version")
    //navigation
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha02")
}