plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.secta9ine.rest.did"
    compileSdk = 31

    defaultConfig {
        applicationId = "com.secta9ine.rest.did"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.6.0") // Core KTX 라이브러리
    implementation("androidx.compose.ui:ui:1.0.5") // Jetpack Compose UI
    implementation("androidx.compose.material:material:1.0.5") // Jetpack Compose Material
    implementation("androidx.compose.ui:ui-tooling-preview:1.0.5") // Jetpack Compose UI Tooling
    implementation("androidx.activity:activity-compose:1.3.1") // Jetpack Compose Activity
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1") // Lifecycle KTX
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")
    implementation("androidx.test:monitor:1.7.2")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("junit:junit:4.12")

    implementation("androidx.work:work-runtime-ktx:2.7.0")

    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-compiler:2.45")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

}