package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class WeatherMainDTO implements Serializable {
    private String main;
    private String description;

    @Override
    public String toString() {
        return "{" +
               "\n\"main\":\"" + main + "\"," +
               "\n\"description\":\"" + description + "\"" +
               "\n}";
    }

}
