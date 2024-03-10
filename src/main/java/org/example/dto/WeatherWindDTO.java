package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class WeatherWindDTO implements Serializable {
    private int speed;

    @Override
    public String toString() {
        return "{" +
               "\n\"speed\":" + speed +
               "\n}";
    }

}