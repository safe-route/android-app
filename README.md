# Safe Route - Android App

Safe route android app build with kotlin to comunicate with our technology

## Overview

- [Library](#library)
- [Installation](#installation)
- [Structur](#structur)
- [Architecture Pattern](#mvvm-architecture-pattern)
- [Api](#api)
- [Feature](#feature)
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

No definite structure for this project, this is just basic Android Project Structure. go to `mainpackage`.
to access the google maps service, Edit `AndroidManifest.xml` and then place your Google Map Api Key. and also place your Google Map Api Key to this line code `buildConfigField("String", "API_KEY", '"PlAcEYouRAPIkeYHeRE"')` in the Graddle file.

## MVVM Architecture Pattern
![mvvm](https://gitlab.com/safe-route/android-app/-/blob/main/Image/mvvm.png)
This project use mvvm patern for easly to maintain and organizing code.

## Room Database
For costumize the database go to `mainpackage/database/DataRoomDatabase.kt`
`@Database(entities = [DataTraining::class, DataPredict::class], version = 1)`
and update the version

## Api
Note: To use your API go to the folder `mainpackage/api` then customize according to your needs

## Feature

- Statistic
  ![alt text](https://gitlab.com/safe-route/android-app/-/blob/main/Image/home-screen.png)
  ![alt text](https://gitlab.com/safe-route/android-app/-/blob/main/Image/detail-statistic.png)

- Map
  ![alt text](https://gitlab.com/safe-route/android-app/-/blob/main/Image/map-screen.png)
  ![centroid](https://gitlab.com/safe-route/android-app/-/blob/main/Image/centroid-danger-screen.png)
  ![routing](https://gitlab.com/safe-route/android-app/-/blob/main/Image/routing-screen.png)

- Report
  ![report crime](https://gitlab.com/safe-route/android-app/-/blob/main/Image/report-sreen.png)

## Known Issue
- Handle background service efficiently
- Versioning Roomdatabase
- UI/UX

## Reference

- [https://developer.android.com/kotlin](https://developer.android.com/kotlin)
- [https://developers.google.com/maps/documentation/android-sdk](https://developers.google.com/maps/documentation/android-sdk)
