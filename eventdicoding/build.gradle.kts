// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}

buildscript {
    dependencies {
        // Gunakan hanya satu versi untuk plugin Navigation Safe Args
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
    }
}


