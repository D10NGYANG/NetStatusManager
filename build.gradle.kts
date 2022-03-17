plugins {
    id("com.android.application") version android_build_ver apply false
    id("com.android.library") version android_build_ver apply false
    id("org.jetbrains.kotlin.android") version kotlin_ver apply false
}

tasks.register<Delete>(name = "clean") {
    group = "build"
    delete(rootProject.buildDir)
}
