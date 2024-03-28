plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.captioner"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.captioner"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.squareup.okhttp3:okhttp-urlconnection:4.9.0")
    implementation ("com.jakewharton.timber:timber:4.7.1")
    implementation ("com.facebook.stetho:stetho:1.6.0")
    implementation ("com.facebook.stetho:stetho-okhttp3:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation ("com.jakewharton.threetenabp:threetenabp:1.3.1")
    implementation("androidx.preference:preference:1.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("io.github.cymchad:BaseRecyclerViewAdapterHelper:3.0.14")
    implementation ("com.blankj:utilcodex:1.31.1")

}