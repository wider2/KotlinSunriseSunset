Kotlin based project to show sunrise / sunset time of your current place or make search by city.

To reduce number of network connections, I use Google Room database to store search results.

### Platform and tools:
    Android Studio 4
    Kotlin language
    MVVM View model
    RxJava2 for asynchronous events
    Retrofit2 for network connections
    Google Room as local SQL database
    AndroidX ConstraintLayout UI
    Threeteen datetime library
    
### Testing
    Unit test
    Activity tests
    Mockk library instead of Mockito
    UI Espresso test
    Room database test

### Development process
    1. Detect network connection
    2. Determine local GPS coordinates
    3. Get Api request to server with current coordinates to get time of sunrise and sunset
    4. Provide search ability to get coordinates by city name
    5. Store search result in Room database to prevent redundant network request

### Google Places API - not used in this application.
    No reason to pay the bills due to alternative services on the market.
    I prefer to use open source project with MIT license or similar.
    OpenStreetMap Nominatim API Endpoints service provide search by city name to get its coordinate and other data.

### Time zones
    The most tricky part was wrong timezone of the date, because Sunrise Api server returns date in UTC 00:00.
    Therefore I have used LatLongToTimezone open source project, that allows to define timezone by coordinate.
    https://github.com/drtimcooper/LatLongToTimezone
