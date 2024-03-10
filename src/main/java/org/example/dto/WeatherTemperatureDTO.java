package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class WeatherTemperatureDTO implements Serializable {
    private double temp;
    private double feels_like;

    @Override
    public String toString() {
        return "{" +
               "\n\"temp\":" + temp + "," +
               "\n\"feels_like\":" + feels_like +
               "\n}";
    }

}
