load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def rules_cc():
    RULES_CC_VERSION = "0.0.9"
    RULES_CC_SHA = "2037875b9a4456dce4a79d112a8ae885bbc4aad968e6587dca6e64f3a0900cdf"

    http_archive(
        name = "rules_cc",
        url = "https://github.com/bazelbuild/rules_cc/archive/refs/tags/%s.tar.gz" % RULES_CC_VERSION,
        sha256 = RULES_CC_SHA,
        strip_prefix = "rules_cc-%s" % RULES_CC_VERSION
    )

def rules_foreign_cc():
    RULES_FOREIGN_CC_VERSION = "0.9.0"
    RULES_FOREIGN_CC_SHA = "2a4d07cd64b0719b39a7c12218a3e507672b82a97b98c6a89d38565894cf7c51"

    http_archive(
        name = "rules_foreign_cc",
        url = "https://github.com/bazelbuild/rules_foreign_cc/archive/refs/tags/%s.tar.gz" % RULES_FOREIGN_CC_VERSION,
        sha256 = RULES_FOREIGN_CC_SHA,
        strip_prefix = "rules_foreign_cc-%s" % RULES_FOREIGN_CC_VERSION
    )

def rules_proto_grpc():
    RULES_PROTO_GRPC_VERSION = "4.5.0"
    RULES_PROTO_GRPC_SHA = "9ba7299c5eb6ec45b6b9a0ceb9916d0ab96789ac8218269322f0124c0c0d24e2"

    http_archive(
        name = "rules_proto_grpc",
        sha256 = RULES_PROTO_GRPC_SHA,
        strip_prefix = "rules_proto_grpc-%s" % RULES_PROTO_GRPC_VERSION,
        url = "https://github.com/rules-proto-grpc/rules_proto_grpc/releases/download/%s/rules_proto_grpc-%s.tar.gz" % (RULES_PROTO_GRPC_VERSION, RULES_PROTO_GRPC_VERSION),
    )

    # load("@rules_proto_grpc//:repositories.bzl", "rules_proto_grpc_toolchains", "rules_proto_grpc_repos")
    # rules_proto_grpc_toolchains()
    # rules_proto_grpc_repos()

    # load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")
    # rules_proto_dependencies()
    # rules_proto_toolchains()

def glog():
    http_archive(
        name = "com_github_glog_glog_no_gflags",
        strip_prefix = "glog-0.6.0",
        sha256 = "122fb6b712808ef43fbf80f75c52a21c9760683dae470154f02bddfc61135022",
        build_file = "@mediapipe//third_party:glog_no_gflags.BUILD",
        url = "https://github.com/google/glog/archive/v0.6.0.zip",
        #patches = [ "@mediapipe//third_party:com_github_glog_glog.diff" ],
        #patch_args = [ "-p1" ],
    )

def stblib():
    http_archive(
        name = "stblib",
        strip_prefix = "stb-b42009b3b9d4ca35bc703f5310eedc74f584be58",
        sha256 = "13a99ad430e930907f5611325ec384168a958bf7610e63e60e2fd8e7b7379610",
        urls = ["https://github.com/nothings/stb/archive/b42009b3b9d4ca35bc703f5310eedc74f584be58.tar.gz"],
        build_file = "@mediapipe//third_party:stblib.BUILD",
        patches = [ "@mediapipe//third_party:stb_image_impl.diff" ],
        patch_args = [ "-p1" ],
    )


def mediapipe():
    MEDIAPIPE_VERSION = "0.10.5"
    MEDIAPIPE_SHA = "14960f99787dd26b93ca69d29f00d8bfada1a0503410e940abfd4390e64e445a"

    http_archive(
        name = "mediapipe",
        patch_args = [ "-p1" ],
        patches = [ "@//third_party/mediapipe:mediapipe_patch.diff" ],
        sha256 = MEDIAPIPE_SHA,
        strip_prefix = "mediapipe-%s" % MEDIAPIPE_VERSION,
        url = "https://github.com/google/mediapipe/archive/refs/tags/v%s.zip" % MEDIAPIPE_VERSION,
    )

def mediapipe_dependencies():
    rules_cc()
    rules_foreign_cc()
    rules_proto_grpc()
    glog()
    stblib()
    mediapipe()