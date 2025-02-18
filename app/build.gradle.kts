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
        kotlinCompilerExtensionVersion = "1.4.2"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        baseline = file("lint-baseline.xml")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.6.0") // Core KTX 라이브러리
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1") // Lifecycle KTX
    implementation("androidx.compose.ui:ui:1.1.0") // Jetpack Compose UI
    implementation("androidx.compose.material:material:1.1.0") // Jetpack Compose Material
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    implementation("androidx.compose.ui:ui-tooling-preview:1.0.5") // Jetpack Compose UI Tooling
    implementation("androidx.activity:activity-compose:1.3.1") // Jetpack Compose Activity

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")
    implementation("androidx.test:monitor:1.7.2")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.test.ext:junit-ktx:1.2.1")
    implementation("androidx.compose.foundation:foundation:1.1.0")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("junit:junit:4.12")

    implementation("androidx.work:work-runtime-ktx:2.7.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-compiler:2.46.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // Room
    implementation("androidx.room:room-runtime:2.4.3")
    kapt("androidx.room:room-compiler:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

}