bazelisk build \
    --config=android_arm64 \
    --spawn_strategy=local \
    --platforms=@//:android_arm64 \
    //project_guideline/android/java/com/google/research/guideline/engine:native_libs