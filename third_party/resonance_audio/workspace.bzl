load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def libogg():
    http_archive(
        name = "libogg",
        build_file = "//third_party/resonance_audio:libogg.BUILD",
        patch_args = [ "-p1" ],
        patches = [ "//third_party/resonance_audio:libogg_patch.diff" ],
        sha256 = "0eb4b4b9420a0f51db142ba3f9c64b333f826532dc0f48c6410ae51f4799b664",
        strip_prefix = "libogg-1.3.5",
        urls = ["https://downloads.xiph.org/releases/ogg/libogg-1.3.5.tar.gz"],
    )

def libvorbis():
    http_archive(
        name = "libvorbis",
        build_file = "//third_party/resonance_audio:libvorbis.BUILD",
        #patch_args = [ "-p1" ],
        #patches = [ "//third_party/resonance_audio:libvorbis_patch.diff" ],
        sha256 = "0e982409a9c3fc82ee06e08205b1355e5c6aa4c36bca58146ef399621b0ce5ab",
        strip_prefix = "libvorbis-1.3.7",
        url = "https://ftp.osuosl.org/pub/xiph/releases/vorbis/libvorbis-1.3.7.tar.gz",
    )

def pffft():
    http_archive(
        name = "pffft",
        build_file = "//third_party/resonance_audio:pffft.BUILD",
        strip_prefix = "jpommier-pffft-7c3b5a7dc510",
        url = "https://bitbucket.org/jpommier/pffft/get/7c3b5a7dc510.zip",
    )

def resonance_audio():
    http_archive(
        name = "resonance_audio",
        build_file = "//third_party/resonance_audio:resonance_audio.BUILD",
        sha256 = "f7f3fbb53dbcd4f14812a7e4c2825c37423f45487a4710a34ff3f922a5efec77",
        strip_prefix = "resonance-audio-4556a46afd4ffae092aa281bfd072eb0279d3a29",
        urls = ["https://github.com/resonance-audio/resonance-audio/archive/4556a46afd4ffae092aa281bfd072eb0279d3a29.tar.gz"],
    )

def resonance_audio_dependencies():
    libogg()
    libvorbis()
    pffft()
    resonance_audio()