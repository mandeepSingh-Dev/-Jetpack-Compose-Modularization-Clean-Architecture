plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.uae.assist"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.uae.assist"
        minSdk = 26
        targetSdk = 37
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
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    //MODULE: core-navigation
    implementation(project(":core-navigation"))
    implementation(project(":core-common"))
    implementation(project(":feature-auth"))
    implementation(project(":feature-profile"))
    implementation(project(":feature-home"))
    implementation(project(":feature-chat_with_us"))
    implementation(project(":feature-location"))
    implementation(project(":core-network"))
    implementation(project(":core-socket"))
    implementation(project(":core-location"))


    //Navigation 3
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.core)


    //DAGGER-HILT
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //Coil
    implementation(libs.coil.compose)
//    implementation(libs.coil.network.okhttp)

    //Socket
    implementation("io.socket:socket.io-client:2.1.2")


//AWS S3
    implementation(libs.aws.android.sdk.s3) // Use the latest version
    implementation(libs.core)

    //Retrofit
    implementation(libs.retrofit)
     //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    //Okhttp Logging Interceptor
    implementation(libs.logging.interceptor)


//Datastore
    implementation(libs.androidx.datastore.preferences)

    //GSON
    implementation("com.google.code.gson:gson:2.14.0")

    //Constraint-Layout
    implementation(libs.androidx.constraintlayout.compose)

    implementation(libs.material3)
    implementation(libs.material.icons.core)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.androidx.hilt.navigation.compose)


    implementation(libs.firebase.messaging)

}