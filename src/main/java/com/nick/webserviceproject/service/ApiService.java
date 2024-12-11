package com.nick.webserviceproject.service;

import com.nick.webserviceproject.dto.current.WeatherDataDTO;
import com.nick.webserviceproject.dto.forecast.WeatherDataForecastDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final String apiKey;

    public ApiService(RestTemplate restTemplate, @Value("${apiKey}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public WeatherDataDTO fetchCurrentWeather(String location) {
        String url = "https://api.tomorrow.io/v4/weather/realtime?location=" + location + "&apikey=" + apiKey;
        return restTemplate.getForObject(url, WeatherDataDTO.class);
    }

    public WeatherDataForecastDTO fetchForecastWeather(String location) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime end = now.plusDays(7);

        String startTime = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String endTime = end.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String url = "https://api.tomorrow.io/v4/weather/forecast"
                + "?location=" + location
                + "&timesteps=daily"
                + "&startTime=" + startTime
                + "&endTime=" + endTime
                + "&apikey=" + apiKey;

        return restTemplate.getForObject(url, WeatherDataForecastDTO.class);
    }
}
