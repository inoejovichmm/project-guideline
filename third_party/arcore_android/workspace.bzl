load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def arcore_android_dependencies():
    http_archive(
        name = "arcore_android_aar",
        build_file = "//third_party/arcore_android:arcore_android_aar.BUILD",
        sha256 = "1c99dbb7c14a67a3e157e45d04744cac7484d1f1afdace56ce3f47c122935f9f",
        type = "zip",
        url = "https://maven.google.com/com/google/ar/core/1.36.0/core-1.36.0.aar",
    )

    http_archive(
        name = "arcore_android_sdk",
        build_file = "//third_party/arcore_android:arcore_android_sdk.BUILD",
        sha256 = "4451b8965741a68da4a9bdda24a0cb87f8d181009086bd7a221350afa26589e1",
        strip_prefix = "arcore-android-sdk-1.36.0",
        urls = ["https://github.com/google-ar/arcore-android-sdk/archive/refs/tags/v1.36.0.tar.gz"],
    )