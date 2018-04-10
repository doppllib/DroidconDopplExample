# Overview

This is the Droidcon NYC 2017 app. This will deploy on both Android and iOS using [J2objc](https://github.com/google/j2objc)
 and the [J2objc Gradle](https://j2objcgradle.github.io/) plugin. Shared architecture and libraries supported by [Doppl libraries](https://doppllib.github.io/).

The 2017 app is a refactor that updates the application to use more modern architecture and libraries. These include Android Architecture Components, Room DB, RxJava 2, Retrofit 2.

# CI Status

CI run on all commits against the Android and iOS builds separately. The J2objc build translates shared code first, then 
runs tests against that.

## J2objc/iOS
[![Build status](https://build.appcenter.ms/v0.1/apps/75d67777-b806-4d55-9029-4ed199653717/branches/master/badge)](https://appcenter.ms)

## Android
[![Build status](https://build.appcenter.ms/v0.1/apps/0b5a4973-92e0-4cac-847f-9fbd62b33d1f/branches/master/badge)](https://appcenter.ms)

# Getting Started with iOS

Follow the [J2objc Gradle tutorials](https://j2objcgradle.github.io/basicquickstart.html) to make sure you have j2objc installed and configured.

Clone the repo (obv)

Open terminal and run

```bash
./gradlew j2objcBuild
```

This will transform the shared Java code and compile dependencies.

cd into the ios directory and run

```bash
pod install
```

Assuming that goes to plan, in Finder, navigate to the ios directory, and open the ios workspace project for Xcode. Double-click on it.

![Finding workspace in Finder](docs/findworkspace.gif)

Pick a simulator and run app!

# Getting Started with Android

The Android app should open and build like any other Android app, which is kind of the point.

### BTW

We've committed some api config files that you probably wouldn't commit to your own production code. fabric.properties and similar. Please
be kind. Just didn't want to add setup steps to try app code.
