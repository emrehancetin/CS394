plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.androidx.navigation.safe.args)  // Safe Args plugin
}

android {
    namespace = "com.emrehancetin.cs394"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.emrehancetin.cs394"
        minSdk = 24
        targetSdk = 34
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
    buildFeatures{
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx) // Navigation fragment dependency
    implementation(libs.androidx.navigation.ui.ktx) // Navigation UI dependency

    // Lifecycle dependencies
    implementation(libs.androidx.lifecycle.livedata.ktx)   // LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)  // ViewModel
    implementation(libs.androidx.lifecycle.runtime.ktx)    // Optional, for lifecycle-aware components

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}