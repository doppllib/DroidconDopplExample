# Overview

This is a trimmed down version of the Droidcon NYC app. The production version had several dependencies on 3rd party services, including our proprietary server. Setup would've taken a lot of effort. This version does not log in, and does not store anything on the server. As such, the only thing you *might* need to sign up for is a jwplayer trial account to get the video players working.

## Library Status

Several of these libraries are aging considerably. We'll be replacing and rearchitecting.
Keep an eye on the repos for updates and changes leading up to Droidcon NYC 2017.

## Why?

This is a demo app for Doppl. It's a set of tools and libraries on top of j2objc. [Read Post](https://medium.com/@kpgalligan/doppl-e075a0fde44c)

The basic concepts:

1. Share all non-UI logic, but have native UI's.
2. Leverage native tools, rather than introduce a 3rd language/environment.
3. Minimize first platform development time, which minimizes "waste" effort.

## Parts

This includes both an Android and iOS native app which allows you to view the Droidcon NYC schedule and select talks you'd like to see. Also a few other minor things.

The UI's are native. Most code below the UI is written in Java and converted to Objective-C with [j2objc](http://j2objc.org/), using [Doppl](http://doppl.co/) tools and framework ports.

Shared code handles storage, networking, and logic (mostly). Libraries include gson, retrofit, and various Android db and threading libraries.

Code has been included from j2objc-gradle, the squidb j2objc port, gson, retrofit, rxjava, eventbus, and various Android libraries from the touchlab toolkit. More complete attribution and thanks forthcoming.

# Setup

## Overview

1. Install Android Studio
2. Install Xcode
3. Install j2objc runtime (slightly modded version)
4. Run Android
5. Run iOS

## Install Android Studio

Grab the latest version (2.3). Stable should work generally, but progress! You may need to downgrade the gradle version to run in older Android Studio versions.

[Android Studio](https://developer.android.com/studio/index.html)

## Install Xcode

You need to be on a Mac.

### Install Carthage

You need Carthage to get Kingfisher for the app. It's not required for any of the j2objc/doppl stuff. See the Kingfisher page for details, or change install method. Just need the framework for Swift code.

[Kingfisher Install](https://github.com/onevcat/Kingfisher/wiki/Installation-Guide)

If using Carthage, cd into the `ios` dir and run `carthage update` which should build Kingfisher.

## Install j2objc runtime

Doppl uses a slightly modified j2objc runtime. This will likely be merged over time to keep the standard j2objc runtime, but for now, grab ours. It's about 1 gig, fyi.

[j2objc runtime](http://dopplmaven.s3-website-us-east-1.amazonaws.com/doppldist.zip)

Extract the zip to a directory (avoid spaces in the path if possible).

[dist dir] is the absolute path where you installed the j2objc zip.

In Xcode, open Xcode > Preferences, Locations > Custom Paths. Add 'J2OBJC_LOCAL_PATH' and set Path to 'dist dir' (no trailing slash).

![xcode paths](https://s3.amazonaws.com/dopplmaven/xcodepath.png)

In Android Studio, look in the root directory for 'local.properties'. This should be generated when you open the project in Android Studio.

Add the following to local.properties in the project directory

```
sdk.dir=[android sdk dir]
j2objc.home=[dist dir]
```

# Verify build works

### Android

Open the project in Android Studio. Run a standard Android build. This should install like any standard android app.

*Hot deploy may have issues, but they were related to the Android libraries and chromecast. Nothing in the cross platform parts.*

### iOS

Assuming Kingfisher is built, you can now generate Objective-C from the Java code. Run the following on the command line at the root of the project.

```
./gradlew dopplDeploy
```

This will build the Objective-C code and copy to the ios folder.

*If you add/remove classes* you'll need to tell Xcode. It won't automatically see them. That's how Xcode works, just FYI.

If you're going to do ongoing java-to-objc dev, I would suggest the following:

```
./gradlew -t dopplDeploy
```

That will trigger the "hot" gradle build, and transform code on the fly. It watches the inputs on the gradle task for relevant changes and runs the necessary transform task. Do that, and Command+Tag over to Xcode.

# Notes

This is an example of using doppl with a separate java jar module. You can include doppl directly in your Android build, but this project is a complex build with some kind of Kotlin issue, so we left it as a separate module.

## Server

The server isn't open source at this time, but it would be relatively simple to modify the app to work with local data and seed it with json. If you'd like to use the server, get in touch (info [at] touchlab.co)

## Squeaky

A fork of ORMLite that is Android specific and uses source generation instead of reflection. Performance is significantly improved. Wound up not really "releasing" this, but it works.

## Open Source Tools

[Picasso](http://square.github.io/picasso/)

[Retrofit](http://square.github.io/retrofit/)

[OkHttp](http://square.github.io/okhttp/)

[EventBus](https://github.com/greenrobot/EventBus)

[SuperToasts](https://github.com/JohnPersano/SuperToasts)

[VectorCompat](https://github.com/wnafee/vector-compat)

[Circle Image View](https://github.com/hdodenhof/CircleImageView)
