plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.translatorkmm.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.translatorkmm.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.translatorkmm.TestHiltRunner"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.ktor.android)

    implementation(libs.compose.navigation)

    implementation(libs.compose.coil)

    implementation(libs.kotlin.dateTime)

    implementation(libs.hilt.android)
    kapt(libs.hilt.androidCompiler)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigationCompose)

    kaptAndroidTest(libs.hilt.androidCompiler)

    androidTestImplementation(libs.hilt.testing)
    androidTestImplementation(libs.compose.testing)
    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.test.jUnit)
    androidTestImplementation(libs.test.rules)

    debugImplementation(libs.compose.testManifest)

}