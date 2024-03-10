package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class WeatherSysDTO implements Serializable {
    private long sunrise;
    private long sunset;

    @Override
    public String toString() {
        return "{" +
               "\n\"sunrise\":" + sunrise + "," +
               "\n\"sunset\":" + sunset +
               "\n}";
    }

}
