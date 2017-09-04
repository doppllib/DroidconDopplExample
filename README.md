# Overview

This is the Droidcon NYC 2017 app. This will deploy on both Android and iOS using http://doppl.co/ code sharing technology.

The 2017 app is a refactor that updates the application to use more modern architecture and libraries. These include Android Architecture Components, Room DB, RxJava 2, Retrofit 2.

# Getting Started

Follow the [Doppl Tutorial](http://doppl.co/docs/quicktutorial.html) to make sure you have j2objc installed and configured.

Install [CocoaPods](https://cocoapods.org/). This is not needed for Doppl, but the Droidcon app uses Firebase, which is *much* easier to install with CocoaPods than manually.

Clone the repo (obv)

Open terminal to ios directory and run

```bash
pod install
```

Look for the following somewhere on output

```bash
Pod installation complete! There are 3 dependencies from the Podfile and 9 total pods installed.
```

cd back to the home directory (of the repo), and run

```bash
./gradlew dopplDeploy
```

Assuming that goes to plan, in Finder, navigate to the ios directory, and open the ios workspace project for Xcode. Double-click on it.

![Finding workspace in Finder](docs/findworkspace.gif)

Pick a simulator and run app!
