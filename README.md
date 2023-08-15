<p align="center">
  <h1 align="center">DisasterAlert</h1>
  <h3 align="center">ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi</h3>
  <div align="center">
    <a href="https://android-arsenal.com/api?level=26">
      <img src="https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat" alt="API Level" />
    </a>
  </div>

  <p align="left">
    DisasterAlert is an Android-based disaster notification application. This application can filter based on location, disaster, or time period and will be displayed on the available map.
  </p>
</p>

## Feature
1. Disaster data is updated in real-time.
2. Interactive maps that showing disaster location and detail.
3. Detail information list such as disaster type, location, and date.
4. Filtering disaster by Location, Time Period, and Disaster Type (Flood, Earthquake, Fire, Haze, Wind, Volcano).
5. Push notification for warning information

## Dependencies Used
The following dependencies are used in the project,
| Name | Version |
|--|--|
| [Kotlin](https://kotlinlang.org/) | 1.8.0 |
| [Glide](https://bumptech.github.io/glide/) | 4.15.1 |
| [Hilt](https://dagger.dev/hilt/) | 2.44 |
| [Google Maps SDK for Android](https://developers.google.com/maps/documentation/android-sdk) | 18.1.0 |
| [Preferences Datastore](https://developer.android.com/jetpack/androidx/releases/datastore) | 1.0.0 |
| [Room Database](https://developer.android.com/jetpack/androidx/releases/room) | 2.5.2 |
| [Retrofit 2](https://square.github.io/retrofit/) | 2.9.0 |
| [Gson Converter](https://square.github.io/retrofit/) | 2.9.0 |
| [OkHttp 3](https://square.github.io/okhttp/) | 4.9.3 |
| [Work Manager](https://developer.android.com/jetpack/androidx/releases/work) | 2.8.1 |
| [MockK](https://mockk.io/) | 1.13.5 |
| [Kotlinx Coroutines Test](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/) | 1.7.3 |
| [Flipper](https://fbflipper.com/docs/features/) | 0.201.0 |
| [SoLoader](https://github.com/facebook/SoLoader) | 0.10.5 |

## Getting Started
1. Fetch the latest source code from the master branch.

``` 
git clone https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi.git
```

2. Add api key.

<ul>

DisasterAlert uses the GoogleMaps API to fetch the needed data, so before using it you have to create an account on [Google Maps Platform website](https://mapsplatform.google.com/), then you can get your api key.

- Go to `local.properties`
- Add code like this
```kotlin
MAPS_API_KEY=XXXXXXXXXX;
```
- Replace the all `XX..` to your own api key.
- Don't forget to rebuild your project after add your api key.
- It's done!
</ul>

3. Run the app with Android Studio.

## User Interface
### Light Mode
<img src="https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/a3492cc5-e3c4-403e-8146-c1a291155fc2" width="25%"> <img src="https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/fbbd704e-e7de-465c-aeb6-a20f71da8be8" width="25%"> <img src="https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/03f7cfb6-0f53-49f9-8723-0c63ee856962" width="25%"> <img src="https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/e6f80465-a057-46c3-898d-be0281ed5e25" width="25%"> <img src="https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/2beb1111-2c01-46ec-abc1-7c878c8ac3e5" width="25%"> <img src="https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/ac2131b7-ea8d-41be-8882-7ed3c94a0f2d" width="25%">

### Dark mode
<img src="https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/5d6593ec-e2e1-4b4d-9240-51dfe5286d6b" width="25%"> <img src="https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/5e3133ef-0669-401b-a20e-ce1e5df7f894" width="25%"> <img src="https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/470b26a0-527f-4ae9-9b7c-9e0dc7aefec6" width="25%"> <img src="https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/9beddf5d-fd1c-4e24-921a-f695ee35cdf5" width="25%"> <img src="https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/452bb743-6d8e-407e-8f32-51d5dd0112e0" width="25%"> <img src="https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/67a42eae-8c83-42f9-93a9-fe7a074e7ce7" width="25%">

## Testing Documentation
![image](https://github.com/GG-3-0-Mobile-Engineering/ME14-DisasterAlert-GG3MEGP0578-MuhammadAnjarHarimurtiRahadi/assets/49654730/75051dea-1abf-46d0-8b7a-93c5ee02a74c)

## Demo App
[Click Here](https://youtu.be/2W_zeEO5cuM) 

## Download The Latest App
[Click Here](https://drive.google.com/file/d/1fYjBimtFSC_1f82Iro0T-zbvLkops9G1/view?usp=sharing) 

## Author
This project is developed by [Anjar Harimurti](https://github.com/args06).

## Contact
You can reach out to me directly at [E-mail](mailto:<anjarharimurti.ah@gmail.com>).

<div align="center">

### Show some support by starring 🌟 the repository! Thanks fo visitting.

</div>
