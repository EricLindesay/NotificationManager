// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
//    alias(libs.plugins.ksp)    id("com.google.devtools.ksp") version "1.9.20-1.0.14" // or compatible with your AGP
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    alias(libs.plugins.kotlin.compose) apply false

}

//allprojects {
//    repositories {
//        maven {url=uri("https://www.jitpack.io")}
//    }
//}
