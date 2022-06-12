# Safe Route - Android App

Safe route android app build with kotlin to comunicate with our technology that we buil

## Overview

- [Library](#library)
- [Installation](#installation)
- [Structur](#structur)
- [Api](#api)
- [Known Issue](#known-issue)
- [Reference](#reference)

## Library

in this app we use library such as:

- [Retrofit](https://github.com/square/retrofit)
- [Circle Image View](https://github.com/hdodenhof/CircleImageView)
- [Glide](https://github.com/bumptech/glide)
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
- [Android Jetpack](https://developer.android.com/jetpack)

## Installation

Note: Before installing this project to your machine make sure minimum you have installed [Android Studio Bumblebee (2021.1.1) Stable](https://android-developers.googleblog.com/2022/01/android-studio-bumblebee-202111-stable.html)

```
git clone https://gitlab.com/safe-route/android-app.git
```

then sync the Graddle or Build > Rebuild Project

## Structur

No definite structure for this project, this is just basic Android Project Structure.
to access the google maps service, Edit `AndroidManifest.xml` and then place your Google Map Api Key. and also place your Google Map Api Key to this line code `buildConfigField("String", "API_KEY", '"PlAcEYouRAPIkeYHeRE"')` in the Graddle file.

## Api

To use your API go to the folder `mainpackage/api` then customize according to your needs

## Known Issue

- Handle background service efficiently
- Versioning Roomdatabase
- UI/UX

## Reference

- [https://developer.android.com/kotlin](https://developer.android.com/kotlin)
- [https://developers.google.com/maps/documentation/android-sdk](https://developers.google.com/maps/documentation/android-sdk)
