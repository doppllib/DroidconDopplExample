# Overview

This is a trimmed down version of the Droidcon NYC app. The production version had several dependencies
on 3rd party services, including our proprietary server. Setup would've taken a lot of effort.
This version does not log in, and does not store anything on the server. As such, the only thing
you *might* need to sign up for is a jwplayer trial account to get the video players working.

# Why?

This is a demo app for our project building tools and libraries on top of j2objc, to facilitate
cross platform development. Its called Doppl. Short for "doppelganger". [Read Post](https://medium.com/@kpgalligan/doppl-e075a0fde44c)

The basic concepts:

1. Share all non-UI logic, but have native UI's.
2. Leverage native tools, rather than introduce a 3rd language/environment.
3. Minimize first platform development time, which minimizes "waste" effort.

Anyway...

# Parts

This includes both an Android and iOS native app which allows you to view the Droidcon NYC schedule
and select talks you'd like to see. Also a few other minor things.

The UI's are native. Most code below the UI is written in Java and converted to Objective-C with
[j2objc](http://j2objc.org/), using [Doppl](http://doppl.co/) tools and framework ports.

Shared code handles storage, networking, and logic (mostly). Libraries include gson, retrofit, and
various Android db and threading libraries.

Code has been included from j2objc-gradle, the squidb j2objc port, gson, retrofit, rxjava, eventbus,
and various Android libraries from the touchlab toolkit. More complete attribution and thanks forthcoming.

# Setup

*Doppl build tools are in flux, so some of these details will change in the near future*

## Overview

1. Install Android Studio
2. Install Xcode
3. Install j2objc runtime (slightly modded version)
4. Run Android
5. Run iOS

## Install Android Studio

Grab the latest stable release, currently version 2.2.2.

[Android Studio](https://developer.android.com/studio/index.html)

## Install Xcode

Install Xcode. I would suggest finding and running a simple sample project, to minimize potential issues with the iOS build system.

### Install Carthage

You need Carthage to get Kingfisher for the app. Its not required for any of the j2objc/doppl stuff.
See the Kingfisher page for details, or change install method. Just need the framework for swift code.

[Kingfisher Install](https://github.com/onevcat/Kingfisher/wiki/Installation-Guide)

If using Carthage, cd into the ios dir and run 'carthage update' which should build Kingfisher.

## Install j2objc runtime

Doppl uses a slightly modified j2objc runtime. This will likely be merged over time to keep the standard
j2objc runtime, but for now, grab ours. Its about 1 gig, fyi.

[j2objc runtime](http://dopplmaven.s3-website-us-east-1.amazonaws.com/dist.zip)

Extract the zip to a directory (avoid spaces in the path if possible).

[dist dir] is the absolute path where you installed the j2objc zip

In Xcode, open XCode > Preferences, Locations > Custom Paths. Add 'J2OBJC_LOCAL_PATH' and set Path
to 'dist dir' (no trailing slash).

![xcode paths](https://s3.amazonaws.com/dopplmaven/xcodepath.png)

Add the following to local.properties in the project directory

```
sdk.dir=[android sdk dir]
j2objc.home=[dist dir]
baseurl=https://droidcon-server.herokuapp.com/
jwLicenseKey=[get a key from jwplayer.com]
releasePassword=[You make this. Only needed for release builds]
```

## Verify build works

### Android

Open the project in Android Studio. Make sure to select the "albanyDebug" flavor/variant (this will
be merged down to a single flavor soon). Run a standard Android build. This should install like any
standard android app.

*Hot deploy may have issues, but they were related to the Android libraries and chromecast. Nothing
in the cross platform parts*

### iOS

Assuming Kingfisher is built, you *should* be able to run without modificaion. The j2objc/doppl files
should be available. Open the project in the 'ios' folder and run.

If you make changes to the java code, run the following on the project command line:

./gradlew doppelArchive

This will build the objective c code and copy to the ios folder.

*If you add/remove classes* you'll need to tell Xcode. It won't automatically see them. That's how Xcode rolls, just FYI.

# Notes

There was a bug moving to swift 3 with html formatting strings. That's to be fixed.

The public Android app actually has some pretty bad reviews. Apparently there's an issue with the
vector support and Android 4.1 (ish). We didn't test on 4.1, but also assumed there wouldn't be
many people at an Android dev conference with 4.1 phones. May fix, or set minimum to 5.

The code is very much not a universal example of best practice. The droidcon nyc app has always
been a strange mix of libraries and tech. The Android side uses lots of Kotlin, and the ios side
has code that represented our first tests of j2objc all the way to latest libraries. Patterns will
see inconsistent as a result. However, in general we're using an MVP pattern to push as much logic
as possible in the shared layer.

Also, no tests. Need tests.

We'll push out either a refreshed version of this, or a new sample app in the next few weeks with
better examples.

# Other Stuff

Notes from the original Droidcon README

#################################

# Server

The server isn't open source at this time, but it would be relatively simple to modify the app to
work with local data and seed it with json.  If you'd like to use the server, get in touch (info [at] touchlab.co)

# Code

### Kotlin

Kotlin has been updated to v1!

A large portion of the app is written in [Kotlin](http://kotlinlang.org/).  Android Studio support is pretty good at this point,
although some parts need to be Java.  Specifically anything that runs through annotation processing.

### Squeaky

A fork of ORMLite that is Android specific and uses source generation instead of reflection.  Performance
is significantly improved, and it supports immutable fields as well as views.  Note *quite* open source
yet.  Need to clean things up a bit.  Was going to announce it at the conference, but time ran short.  You can't actually build the app as-is with this, but either wait till the source is up, or just swap out with ORMLite.  Its mostly compatible, after package renames (and some tweaking of a foreign collection).

Also, name isn't final.  See how that goes.

## Open Source Tools

[Picasso](http://square.github.io/picasso/)

[Retrofit](http://square.github.io/retrofit/)

[OKHttp](http://square.github.io/okhttp/)

[EventBus](https://github.com/greenrobot/EventBus)

[SuperToasts](https://github.com/JohnPersano/SuperToasts)

[VectorCompat](https://github.com/wnafee/vector-compat)

[Circle Image View](https://github.com/hdodenhof/CircleImageView)
