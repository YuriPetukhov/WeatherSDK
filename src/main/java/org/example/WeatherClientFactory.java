package org.example;

import org.example.enums.ModeType;

import java.util.HashMap;
import java.util.Map;import java.util.logging.Logger;

public class WeatherClientFactory {
    private final Logger logger = Logger.getLogger(WeatherClient.class.getName());
    private final Map<String, WeatherClient> weatherClients = new HashMap<>();

    public WeatherClient createWeatherClient(String apiKey, ModeType mode) {
        logger.info("Creating WeatherClient for API key: " + apiKey);
        if (weatherClients.containsKey(apiKey)) {
            return weatherClients.get(apiKey);
        } else {
            WeatherClient weatherClient = new WeatherClient(apiKey, mode);
            weatherClients.put(apiKey, weatherClient);
            return weatherClient;
        }
    }

    public void deleteWeatherClient(String apiKey) {
        logger.info("Deleting WeatherClient for API key: " + apiKey);
        weatherClients.remove(apiKey);
    }
}


