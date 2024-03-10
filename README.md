**Weather API SDK**

**Introduction**

This SDK provides a simple and convenient way to access the OpenWeather API and retrieve weather data for a given location. The SDK is easy to use, with clear and concise documentation, and can be easily integrated into your own applications.

**Installation**

To install the SDK, simply add the following dependency to your project:

```xml
<dependency>
  <groupId>com.example</groupId>
  <artifactId>weather-api-sdk</artifactId>
  <version>1.0.0</version>
</dependency>
```

**Usage**

To use the SDK, first create a `WeatherClient` object by passing your API key and the desired mode (on-demand or polling) to the constructor:

```java
WeatherClient weatherClient = new WeatherClient("YOUR_API_KEY", ModeType.ON_DEMAND);
```

Once you have created a `WeatherClient` object, you can use it to query the weather API for a given city:

```java
WeatherDTO weatherDTO = weatherClient.getWeather("London");
```

The `getWeather()` method will return a `WeatherDTO` object containing the weather data for the specified city. The `WeatherDTO` object contains the following properties:

* `weather`: The current weather conditions (e.g., "Clouds", "Rain")
* `temperature`: The current temperature in Celsius
* `visibility`: The visibility in meters
* `wind`: The wind speed in meters per second
* `datetime`: The time of the weather observation
* `sys`: The sunrise and sunset times
* `timezone`: The timezone offset in seconds
* `name`: The name of the city

**Error Handling**

The SDK will throw an exception if there is an error querying the weather API. The exception will contain a description of the error.

**Examples**

The following is an example of how to use the SDK to retrieve weather data for a given city:

```java
String API_KEY = "YOUR_API_KEY";

        try {
        WeatherClientFactory factory = new WeatherClientFactory();
        WeatherClient weatherClient = factory.createWeatherClient(API_KEY, ModeType.ON_DEMAND);
        System.out.println(weatherClient.getWeather("Berlin"));
        } catch (WeatherServiceException e) {
        System.out.println(e.getMessage());
        }
```

The following is an example of how to use the SDK in polling mode:

```java
String API_KEY = "YOUR_API_KEY";

        try {
        WeatherClientFactory factory = new WeatherClientFactory();
        WeatherClient weatherClient = factory.createWeatherClient(API_KEY, ModeType.POLLING);
        System.out.println(weatherClient.getWeather("London"));
        } catch (WeatherServiceException e) {
        System.out.println(e.getMessage());
        }
```

**Support**

If you have any questions or need help using the SDK, please contact us at petuchowjuri@yahoo.de.