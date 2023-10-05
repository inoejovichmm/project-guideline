#!/bin/bash
set -e

if [ "$(uname)" == "Darwin" ]
then
  platform="darwin"
  platform_android_sdk="mac"
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]
then
  platform="linux"
  platform_android_sdk="linux"
fi

android_sdk_path="$ANDROID_HOME"
android_ndk_path="$ANDROID_HOME/ndk-bundle"
ndk_version="$ANDROID_NDK_VERSION"

if [ -d "$android_sdk_path" ]
then
  echo "Warning: android_sdk_path is non empty. Installation of the Android SDK will be skipped."
else
  rm -rf /tmp/android_sdk/
  mkdir  /tmp/android_sdk/
  curl https://dl.google.com/android/repository/commandlinetools-${platform_android_sdk}-9477386_latest.zip -o /tmp/android_sdk/commandline_tools.zip
  unzip /tmp/android_sdk/commandline_tools.zip -d /tmp/android_sdk/
  mkdir -p $android_sdk_path
  /tmp/android_sdk/cmdline-tools/bin/sdkmanager --update --sdk_root=${android_sdk_path}
  yes | /tmp/android_sdk/cmdline-tools/bin/sdkmanager --licenses --sdk_root=${android_sdk_path} &>/dev/null
  /tmp/android_sdk/cmdline-tools/bin/sdkmanager "build-tools;33.0.2" "platform-tools" "platforms;android-33" "extras;android;m2repository" --sdk_root=${android_sdk_path}
  rm -rf /tmp/android_sdk/
fi

if [ -d "${android_ndk_path}/android-ndk-${ndk_version}" ]
then
  echo "Warning: android_ndk_path is non empty. Android NDK Installation will be ignored."
else
  rm -rf /tmp/android_ndk/
  mkdir /tmp/android_ndk/
  curl https://dl.google.com/android/repository/android-ndk-${ndk_version}-${platform}-x86_64.zip -o /tmp/android_ndk/android_ndk.zip
  mkdir -p ${android_ndk_path}/android-ndk-${ndk_version}
  unzip -q /tmp/android_ndk/android_ndk.zip -d ${android_ndk_path}
  rm -rf /tmp/android_ndk/
fi