package org.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.example.dto.WeatherDTO;
import org.example.enums.ModeType;
import org.example.exceptions.WeatherServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WeatherClientUnitTest {

    private WeatherClient weatherClient;

    @BeforeEach
    public void setUp() {
        weatherClient = new WeatherClient("my-api-key", ModeType.ON_DEMAND);
    }

    @Test
    public void testGetWeather_CityFound() throws IOException, InterruptedException, WeatherServiceException {
        String cityName = "London";
        String responseBody = "{\"coord\":{\"lon\":-0.1257,\"lat\":51.5085},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\"}],\"base\":\"stations\",\"main\":{\"temp\":282.55,\"feels_like\":281.16,\"temp_min\":280.37,\"temp_max\":284.26,\"pressure\":1012,\"humidity\":81},\"visibility\":10000,\"wind\":{\"speed\":4.1,\"deg\":250},\"clouds\":{\"all\":90},\"dt\":1645826583,\"sys\":{\"type\":1,\"id\":1414,\"country\":\"GB\",\"sunrise\":1645786852,\"sunset\":1645841326},\"timezone\":0,\"id\":2643743,\"name\":\"London\",\"cod\":200}";
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(responseBody);

        HttpClient httpClient = mock(HttpClient.class);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

        WeatherClient weatherClient = new WeatherClient("test_api_key", ModeType.POLLING);
        weatherClient.httpClient = httpClient;

        WeatherDTO weatherDTO = weatherClient.getWeather(cityName);

        assertNotNull(weatherDTO);
        assertEquals(cityName, weatherDTO.getName());
        assertEquals(282.55, weatherDTO.getTemperature().getTemp(), 0.01);
    }

    @Test
    public void testGetWeather_CityNotFound() throws IOException, InterruptedException {
        String cityName = "NonExistentCity";
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(404);

        HttpClient httpClient = mock(HttpClient.class);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

        WeatherClient weatherClient = new WeatherClient("test_api_key", ModeType.POLLING);
        weatherClient.httpClient = httpClient;

        assertThrows(WeatherServiceException.class, () -> weatherClient.getWeather(cityName));
    }

    @Test
    public void testGetWeather_InvalidApiKey() throws IOException, InterruptedException {
        String cityName = "ValidCityName";
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(401);

        HttpClient httpClient = mock(HttpClient.class);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

        WeatherClient weatherClient = new WeatherClient("invalid_api_key", ModeType.POLLING);
        weatherClient.httpClient = httpClient;

        assertThrows(WeatherServiceException.class, () -> weatherClient.getWeather(cityName));
    }

    @Test
    public void testUpdateWeather() {
        WeatherClient weatherClient = mock(WeatherClient.class);
        Map<String, WeatherDTO> weatherDTOMap = new HashMap<>();

        weatherDTOMap.put("London", new WeatherDTO());
        weatherDTOMap.put("Paris", new WeatherDTO());

        weatherClient.weatherDTOMap = weatherDTOMap;

        weatherClient.updateWeather();

        assertEquals(2, weatherClient.weatherDTOMap.size());
        assertTrue(weatherClient.weatherDTOMap.containsKey("London"));
        assertTrue(weatherClient.weatherDTOMap.containsKey("Paris"));
    }

    @Test
    public void testSetMode_Polling() {
        ModeType mode = ModeType.POLLING;

        weatherClient.weatherDTOMap.put("Москва", new WeatherDTO());

        weatherClient.setMode(mode);

        assertFalse(weatherClient.scheduler.isShutdown());
        assertFalse(weatherClient.weatherDTOMap.isEmpty());
    }

    @Test
    public void testSetMode_OnDemand() {
        ModeType mode = ModeType.ON_DEMAND;

        weatherClient.setMode(mode);

        assertTrue(weatherClient.scheduler.isShutdown());
        assertTrue(weatherClient.weatherDTOMap.isEmpty());
    }
}
