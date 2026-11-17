name: Build Fabric Mod

on:
  workflow_dispatch:
  push:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Download Official Fabric Template 26.3
        run: |
          git clone --depth 1 --branch 26.3 https://github.com template || git clone --depth 1 https://github.com template
          mkdir -p template/src/client/java/com/example/aaaaa/client
          cp AaaaaClient.java template/src/client/java/com/example/aaaaa/client/AaaaaClient.java
          sed -i 's/"client": \[.*\]/"client": \["com.example.aaaaa.client.AaaaaClient"\]/' template/src/main/resources/fabric.mod.json || true

      - name: Set up JDK 25
        uses: actions/setup-java@v4
        with:
          java-version: '25'
          distribution: 'temurin'

      - name: Build with Gradle
        run: |
          cd template
          chmod +x gradlew
          ./gradlew build

      - name: Upload Mod JAR
        uses: actions/upload-artifact@v4
        with:
          name: compiled-mod
          path: template/build/libs/*.jar
