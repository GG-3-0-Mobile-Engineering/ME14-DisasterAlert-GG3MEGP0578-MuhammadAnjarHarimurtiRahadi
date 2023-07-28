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

4. Run the app with Android Studio or Visual Studio.

## Author
This project is developed by [Anjar Harimurti](https://github.com/args06).

## Contact
You can reach out to me directly at [E-mail](mailto:<anjarharimurti.ah@gmail.com>).

<div align="center">

### Show some support by starring 🌟 the repository! Thanks fo visitting.

</div>
