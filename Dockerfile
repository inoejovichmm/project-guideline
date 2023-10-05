FROM --platform=linux/amd64 debian:latest

ENV ANDROID_NDK_VERSION=r21e

ENV ANDROID_HOME=/Android/Sdk
ENV ANDROID_NDK_HOME=${ANDROID_HOME}/ndk-bundle/android-ndk-${ANDROID_NDK_VERSION}

ENV USE_BAZEL_VERSION="6.1.2"

RUN apt update && apt install -y \
    curl \
    wget \
    build-essential \
    zip \
    unzip \
    python3 \
    python3-numpy \
    openjdk-17-jdk-headless

RUN wget https://github.com/bazelbuild/bazelisk/releases/download/v1.18.0/bazelisk-linux-amd64 && \
    mv bazelisk-linux-amd64 /usr/local/bin/bazelisk && \
    chmod ugo+x /usr/local/bin/bazelisk

COPY --chmod=777 ./install_devkits.sh /install_devkits.sh
RUN ./install_devkits.sh

RUN bazelisk

WORKDIR /app

# CMD [ "bazelisk", "build", "--config=android_arm64", "--compilation_mode=opt", "--spawn_strategy=local", "/project_guideline/android:guideline_app" ]