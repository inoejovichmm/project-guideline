docker run -it --rm \
    --platform linux/amd64 \
    -v $(pwd):/app project-guideline-build-1 \
    bazelisk build --config=android_arm64 --compilation_mode=opt --spawn_strategy=local //project_guideline/android:guideline_app