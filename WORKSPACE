workspace(name = "project_guideline")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

################ LICENSING ################
http_archive(
    name = "rules_license",
    sha256 = "6157e1e68378532d0241ecd15d3c45f6e5cfd98fc10846045509fb2a7cc9e381",
    url = "https://github.com/bazelbuild/rules_license/releases/download/0.0.4/rules_license-0.0.4.tar.gz",
)
################ END LICENSING ################

################ ZLIB ################
# Load Zlib before initializing TensorFlow to guarantee that the target @zlib//:mini_zlib is available
http_archive(
    name = "zlib",
    build_file = "@mediapipe//third_party:zlib.BUILD",
    sha256 = "b3a24de97a8fdbc835b9833169501030b8977031bcb54b3b3ac13740f846ab30",
    strip_prefix = "zlib-1.2.13",
    url = "http://zlib.net/fossils/zlib-1.2.13.tar.gz",
    patches = [ "@mediapipe//third_party:zlib.diff" ],
    patch_args = [ "-p1" ],
)
################ END ZLIB ################

################ PYTHON ################
RULES_PYTHON_VERSION="0.23.1"
RULES_PYTHON_SHA="84aec9e21cc56fbc7f1335035a71c850d1b9b5cc6ff497306f84cced9a769841"

http_archive(
    name = "rules_python",
    sha256 = RULES_PYTHON_SHA,
    strip_prefix = "rules_python-%s" % RULES_PYTHON_VERSION,
    url = "https://github.com/bazelbuild/rules_python/releases/download/%s/rules_python-%s.tar.gz" % (RULES_PYTHON_VERSION, RULES_PYTHON_VERSION),
)
################ END PYTHON ################

################ PROTOBUF ################
# depends on PYTHON

# Protobuf expects an //external:python_headers target
# bind(
#     name = "python_headers",
#     actual = "@local_config_python//:python_headers",
# )

# http_archive(
#     name = "com_google_protobuf",
#     sha256 = "87407cd28e7a9c95d9f61a098a53cf031109d451a7763e7dd1253abf8b4df422",
#     strip_prefix = "protobuf-3.19.1",
#     url = "https://github.com/protocolbuffers/protobuf/archive/v3.19.1.tar.gz",
#     #patches = [ "@//third_party:com_google_protobuf_fixes.diff" ],
#     #patch_args = [ "-p1" ],
# )
################ END PROTOBUF ################

################ MEDIAPIPE ################
load("@//third_party/mediapipe:workspace.bzl", "mediapipe_dependencies")
mediapipe_dependencies()
################ END MEDIAPIPE ################

################ RULES JVM ################
RULES_JVM_EXTERNAL_TAG = "4.0"
RULES_JVM_EXTERNAL_SHA = "31701ad93dbfe544d597dbe62c9a1fdd76d81d8a9150c2bf1ecf928ecdf97169"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

# load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")
# rules_jvm_external_deps()
# load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")
# rules_jvm_external_setup()
################ END RULES JVM ################

################ MAVEN ################
# depends on RULES JVM
load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "androidx.appcompat:appcompat:1.3.1",
        "com.google.protobuf:protobuf-javalite:3.24.4",
        "androidx.fragment:fragment:1.2.0",
        "androidx.activity:activity:1.2.0",
        # "com.google.ar:core:1.36.0",
        # "javax.annotation:javax.annotation-api:1.3.2",
        # "androidx.annotation:annotation:1.5.0",
        # "com.google.code.findbugs:jsr305:3.0.2",

        "androidx.core:core:1.6.0",
        #"androidx.test:core:1.4.0",
        #"androidx.test.ext:junit:1.1.4",
        #"androidx.test.espresso:espresso-core:3.5.0",
        #"androidx.test.espresso:espresso-intents:3.5.0",
        #"org.robolectric:robolectric:4.9.2",
        #"org.robolectric:shadows-framework:4.9.2",
        "com.google.ar:core:1.36.0",
        "com.google.code.findbugs:jsr305:3.0.2",
        #"com.google.guava:guava:31.1-android",
        #"com.google.truth:truth:1.1.3",
        "javax.annotation:javax.annotation-api:1.3.2",
        "androidx.annotation:annotation:1.5.0",
    ],
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
    fetch_sources = True,
    generate_compat_repositories = True,
    #jetify = True,
    #version_conflict_policy = "pinned",
    #duplicate_version_warning = "error",
)
################ END MAVEN ################

################ ARCORE ################
load("@//third_party/arcore_android:workspace.bzl", "arcore_android_dependencies")
arcore_android_dependencies()
################ END ARCORE ################

################ OPENCV ANDROID ################
http_archive(
    name = "android_opencv",
    build_file = "@mediapipe//third_party:opencv_android.BUILD",
    strip_prefix = "OpenCV-android-sdk",
    type = "zip",
    url = "https://github.com/opencv/opencv/releases/download/3.4.3/opencv-3.4.3-android-sdk.zip",
)
################ END OPENCV ANDROID ################

################ ABSEIL ################
# http_archive(
#   name = "com_google_absl",
#   url = "https://github.com/abseil/abseil-cpp/archive/refs/tags/20230802.1.tar.gz",
#   sha256 = "987ce98f02eefbaf930d6e38ab16aa05737234d7afbab2d5c4ea7adbe50c28ed",
#   strip_prefix = "abseil-cpp-20230802.1",
# )
################ END ABSEIL ################

################ RESONANCE AUDIO ################
load("@//third_party/resonance_audio:workspace.bzl", "resonance_audio_dependencies")
resonance_audio_dependencies()
################ END RESONANCE AUDIO ################

################ TENSORFLOW ################
# TensorFlow repo should always go after the other external dependencies.
TENSORFLOW_COMMIT = "e92261fd4cec0b726692081c4d2966b75abf31dd" # TF on 2023-07-26.
TENSORFLOW_SHA = "478a229bd4ec70a5b568ac23b5ea013d9fca46a47d6c43e30365a0412b9febf4"

http_archive(
    name = "org_tensorflow",
    strip_prefix = "tensorflow-%s" % TENSORFLOW_COMMIT,
    sha256 = TENSORFLOW_SHA,
    url = "https://github.com/tensorflow/tensorflow/archive/%s.tar.gz" % TENSORFLOW_COMMIT,
    patches = [
        "@mediapipe//third_party:org_tensorflow_compatibility_fixes.diff",
        "@mediapipe//third_party:org_tensorflow_system_python.diff",
        # Diff is generated with a script, don't update it manually.
        "@mediapipe//third_party:org_tensorflow_custom_ops.diff",
    ],
    patch_args = [ "-p1" ],
)

load("@org_tensorflow//tensorflow:workspace3.bzl", "tf_workspace3")
tf_workspace3()
load("@org_tensorflow//tensorflow:workspace2.bzl", "tf_workspace2")
tf_workspace2()
################ END TENSORFLOW ################

################ LIBYUV ################
http_archive(
    name = "libyuv",
    # Error: operand type mismatch for `vbroadcastss' caused by commit 8a13626e42f7fdcf3a6acbb0316760ee54cda7d8.
    url = "https://chromium.googlesource.com/libyuv/libyuv/+archive/2525698acba9bf9b701ba6b4d9584291a1f62257.tar.gz",
    build_file = "@mediapipe//third_party:libyuv.BUILD",
)
################ END LIBYUV ################

android_sdk_repository(name = "androidsdk")

android_ndk_repository(
    name = "androidndk",
    api_level = 30
)