

buildscript {
    extra["compose_ui_version"] = "1.3.3"
    extra["kotlin_version"] = "1.9.0"
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.1") // AGP 버전
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:$1.9.0")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.4.1" apply false
    id("com.android.library") version "7.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.46.1" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.10"

}