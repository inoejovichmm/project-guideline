plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.protobuf")
}

android {
    namespace = "com.google.research.guideline"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.google.research.guideline"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += "arm64-v8a"
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.google.ar:core:1.39.0")
    implementation("com.google.protobuf:protobuf-java:3.24.4")
    implementation("com.google.protobuf:protobuf-java-util:3.24.4")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.4"
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

tasks.register<Exec>("buildGuidelineJniLib") {
    workingDir("../..")
    commandLine(
        "bazelisk", "build",
        "--config=android_arm64",
        "--spawn_strategy=local",
        "--platforms=@mediapipe//mediapipe:android_arm64_platform",
        "//project_guideline/android/jni:libguideline_native_jni.so"
    )
}

tasks.register<Copy>("copyGuidelineJniLib") {
    dependsOn("buildGuidelineJniLib")
    from(rootDir.parentFile.resolve("bazel-bin/project_guideline/android/jni/libguideline_native_jni.so"))
    into(projectDir.resolve("src/main/jniLibs/arm64-v8a"))
}

tasks.register<Exec>("buildOpenCVLib") {
    workingDir("../..")
    commandLine(
        "bazelisk", "build",
        "--config=android_arm64",
        "--spawn_strategy=local",
        "--platforms=@mediapipe//mediapipe:android_arm64_platform",
        "@android_opencv//:libopencv_java3_so_arm64-v8a"
    )
}

tasks.register<Copy>("copyOpenCVLib") {
    dependsOn("buildOpenCVLib")
    from(rootDir.parentFile.resolve("bazel-project-guideline/external/android_opencv/sdk/native/libs/arm64-v8a/libopencv_java3.so"))
    into(projectDir.resolve("src/main/jniLibs/arm64-v8a"))
}

tasks.preBuild {
    dependsOn(
        "copyGuidelineJniLib",
        "copyOpenCVLib"
    )
}

tasks.clean {
    delete("src/main/jniLibs/arm64-v8a/libopencv_java3.so")
    delete("src/main/jniLibs/arm64-v8a/libguideline_native_jni.so")
    exec {
        workingDir("../..")
        commandLine("bazelisk", "clean")
    }
}