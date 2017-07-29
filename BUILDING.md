BUILDING Mörder
========

First you shall need:

  0. Java SE Development Kit (JDK).
  1. Android Software Development Kit (Android SDK).
  2. This code.

You should read ./android/build.gradle and note the versions of build tools and platforms required. Gradle takes care of a lot.

Generally you should set JAVA\_HOME and ANDROID\_HOME to the roots of your JDK and Android SDK.

Gradle Tasks
------------

    ./gradlew build

Will do a general build of all flavours. You'll need the keystore setup.

Debug builds
------------

    ./gradlew assembleDebug
    ./gradlew installDebug

Will also try to shove it onto your device via the Android Debugging Bridge (adb).

Release Builds
--------------

Create a keystore.properties file at the top of the git clone.

    storePassword=Password for the keystore file.
    keyPassword=Password for the private key.
    keyAlias=Alias for this key
    storeFile=/path/to/important/file.jks

Now you can use the build target, or more explicitly:

    ./gradlew assembleRelease
    ./gradlew installRelease

