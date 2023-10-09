# Copyright 2023 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#load("@build_bazel_rules_android//android:rules.bzl", "android_library")
load("//project_guideline/bazel:build_defs.bzl", "android_only")

package(
    default_applicable_licenses = ["//:license"],
    default_visibility = [
        "//:internal",
    ],
)

android_library(
    name = "engine",
    srcs = glob(["*.java"]),
    exports_manifest = 1,
    manifest = "AndroidManifest.xml",
    resource_files = glob(["res/**"]),
    deps = [
        "//third_party:androidx_core",
        "//third_party:androidx_fragment",
        "//third_party:com_google_ar_core",
        "//third_party:androidx_annotation",
        "//third_party:javax_annotation",
        "//project_guideline/proto:guideline_engine_config_java_proto_lite",
        "@com_google_protobuf//:protobuf",
        "//third_party:protobuf_javalite",
        "//third_party:com_google_code_findbugs_jsr305",
    ] + android_only(":native_libs"),
)

android_library(
    name = "native_libs",
    exports = [
        ":opencv_native_lib",
        "//project_guideline/android/jni:guideline_jni_lib",
    ],
)

cc_library(
    name = "opencv_native_lib",
    srcs = select({
        "@mediapipe//mediapipe:android_arm64": ["@android_opencv//:libopencv_java3_so_arm64-v8a"],
        "@mediapipe//mediapipe:android_arm": ["@android_opencv//:libopencv_java3_so_armeabi-v7a"],
        "@mediapipe//mediapipe:android_x86": ["@android_opencv//:libopencv_java3_so_x86"],
        "@mediapipe//mediapipe:android_x86_64": ["@android_opencv//:libopencv_java3_so_x86_64"],
        "//conditions:default": [],
    }),
    alwayslink = 1,
)
