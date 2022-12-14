buildscript {
//    ext {
//        compose_version = "1.1.0-beta01"
//    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.2.2" apply false
    id("com.android.library") version "7.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("com.google.gms.google-services") version "4.3.13" apply false
}

tasks.register<Delete>("clean"){
    delete(rootProject.buildDir)
}