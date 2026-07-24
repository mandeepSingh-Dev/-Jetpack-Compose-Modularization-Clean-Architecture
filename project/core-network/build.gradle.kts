plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.uae.core_network"
    compileSdk {
        version = release(37)
    }
    val BASE_URL = "https://api.theuaeassist.com/api/"
//    val BASE_URL = "https://bookyourapps.com/uaeassist/api/"
//    val SOCKET_BASE_URL = "https://bookyourapps.com"
    val SOCKET_BASE_URL = "https://api.theuaeassist.com"


    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"$BASE_URL\"")
        buildConfigField("String", "SOCKET_BASE_URL", "\"$SOCKET_BASE_URL\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures{
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.documentfile)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    //DAGGER-HILT
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //Retrofit
    implementation(libs.retrofit)
    //GSon Converter
    //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    //Okhttp Logging Interceptor
    implementation(libs.logging.interceptor)

    //GSON
    implementation("com.google.code.gson:gson:2.14.0")

    implementation(project(":core-common"))

    coreLibraryDesugaring(libs.desugar.jdk.libs)

}