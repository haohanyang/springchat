#!/bin/bash
cp build.gradle build.gradle.bk

echo "Build client"
echo "
application {
    mainClass = 'haohanyang.springchat.ClientLauncher'
}
" >> build.gradle

./gradlew build -x test

echo "Build server"
rm build.gradle
cp build.gradle.bk build.gradle
echo "
application {
    mainClass = 'haohanyang.springchat.ServerLauncher'
}
" >> build.gradle
./gradlew build -x test