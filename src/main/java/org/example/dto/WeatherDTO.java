package org.example.dto;

import lombok.Data;
import org.json.JSONObject;

@Data
public class WeatherDTO {

    private WeatherMainDTO weather;

    private WeatherTemperatureDTO temperature;

    private int visibility;

    private WeatherWindDTO wind;

    private long datetime;

    private WeatherSysDTO sys;

    private int timezone;

    private String name;

    public static WeatherDTO fromJson(String body) {
        JSONObject jsonObject = new JSONObject(body);
        WeatherDTO weatherDTO = new WeatherDTO();
        weatherDTO.setWeather(new WeatherMainDTO(
                jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"),
                jsonObject.getJSONArray("weather").getJSONObject(0).getString("description")
        ));
        weatherDTO.setTemperature(new WeatherTemperatureDTO(
                jsonObject.getJSONObject("main").getDouble("temp"),
                jsonObject.getJSONObject("main").getDouble("feels_like")
        ));
        weatherDTO.setVisibility(jsonObject.getInt("visibility"));
        weatherDTO.setWind(new WeatherWindDTO(
                jsonObject.getJSONObject("wind").getInt("speed")
        ));
        weatherDTO.setDatetime(jsonObject.getLong("dt"));
        weatherDTO.setTimezone(jsonObject.getInt("timezone"));
        weatherDTO.setSys(new WeatherSysDTO(
                jsonObject.getJSONObject("sys").getLong("sunrise"),
                jsonObject.getJSONObject("sys").getLong("sunset")
        ));
        weatherDTO.setName(jsonObject.getString("name"));

        return weatherDTO;
    }

    @Override
    public String toString() {
        return "{" +
               "\n\"weather\":" + weather +
               ",\n\"temperature\":" + temperature +
               ",\n\"visibility\":" + visibility +
               ",\n\"wind\":" + wind +
               ",\n\"datetime\":" + datetime +
               ",\n\"sys\":" + sys +
               ",\n\"timezone\":" + timezone +
               ",\n\"name\":\"" + name + "\"" +
               "\n}";
    }


}
