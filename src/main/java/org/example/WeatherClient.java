package org.example;

import org.example.dto.WeatherDTO;
import org.example.enums.ModeType;
import org.example.exceptions.WeatherServiceException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;import java.util.logging.Logger;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
public class WeatherClient {
    private final String apiKey;
    HttpClient httpClient;
    private ModeType mode;

    private final Logger logger = Logger.getLogger(WeatherClient.class.getName());
    Map<String, WeatherDTO> weatherDTOMap =
            new LinkedHashMap<>(10, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, WeatherDTO> eldest) {
                    return size() > 10;
                }
            };
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public WeatherClient(String apiKey, ModeType mode) {
        this.apiKey = apiKey;
        httpClient = HttpClient.newBuilder().build();
    }

    public WeatherDTO getWeather(String city) throws WeatherServiceException {
        logger.info("Fetching weather data for city: " + city);
        if (weatherDTOMap.containsKey(city)) {
            WeatherDTO weatherDTO = weatherDTOMap.get(city);
            if (System.currentTimeMillis() - weatherDTO.getDatetime() < TimeUnit.MINUTES.toMillis(10)) {
                return weatherDTO;
            } else {
                weatherDTOMap.remove(city);
                return queryWeather(city);
            }
        }
        return queryWeather(city);
    }

    WeatherDTO queryWeather(String cityName) throws WeatherServiceException {
        HttpRequest weatherRequest = HttpRequest.newBuilder(
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=" + apiKey)
        ).build();
        try {
            HttpResponse<String> response = httpClient.send(weatherRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 404) {
                throw new WeatherServiceException("City not found: " + cityName);
            } else if (response.statusCode() == 401) {
                throw new WeatherServiceException("Invalid API key");
            } else if (response.statusCode() != 200) {
                throw new WeatherServiceException("Failed to fetch weather data for city: " + cityName + ". Status code: " + response.statusCode());
            }
            WeatherDTO weatherDTO = WeatherDTO.fromJson(response.body());
            weatherDTOMap.put(cityName, weatherDTO);
            return weatherDTO;
        } catch (IOException | InterruptedException e) {
            throw new WeatherServiceException("Failed to fetch weather data for city: " + cityName, e);
        }
    }

    public void updateWeather() {
        logger.info("Updating weather data for all cities");
        Set<String> cityNames = new LinkedHashSet<>(weatherDTOMap.keySet());
        for (String cityName : cityNames) {
            try {
                WeatherDTO weatherDTO = queryWeather(cityName);
                weatherDTOMap.put(cityName, weatherDTO);
            } catch (WeatherServiceException e) {
                logger.log(Level.SEVERE, "Error fetching weather for city: " + cityName, e);
            }
        }
    }


    public void setMode(ModeType mode) {
        this.mode = mode;

        if (mode == ModeType.POLLING) {
            logger.info("Setting mode to POLLING. Weather data will be updated every 10 minutes.");
            scheduler.scheduleAtFixedRate(this::updateWeather, 0, 10, TimeUnit.MINUTES);
        } else {
            logger.info("Setting mode to ON_DEMAND. Weather data will only be updated when requested.");
            scheduler.shutdown();
        }
    }

    public void switchMode(ModeType mode) {
        ModeType newMode = mode.toggle();
        logger.info("Switching mode from " + this.mode + " to " + newMode);
        setMode(newMode);
    }
}