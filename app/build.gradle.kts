plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.homepharm"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.homepharm"
        minSdk = 27
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.airbnb.android:lottie:3.7.0")
    implementation("com.google.code.gson:gson:2.8.8")

    // Firebase BOM (Bill of Materials) for version management
    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))

    // Firebase libraries
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database")

    implementation("androidx.annotation:annotation:1.6.0")
    implementation("com.google.android.gms:play-services-maps:18.0.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")



    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}

// Apply the Google Services plugin at the bottom
apply(plugin = "com.google.gms.google-services")
