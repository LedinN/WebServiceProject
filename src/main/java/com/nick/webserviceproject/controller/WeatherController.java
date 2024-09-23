package com.nick.webserviceproject.controller;

import com.nick.webserviceproject.model.current.WeatherDataCurrent;
import com.nick.webserviceproject.model.forecast.WeatherDataForecast;
import com.nick.webserviceproject.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public Mono<ResponseEntity<WeatherDataCurrent>> getCurrentWeather(
            @RequestParam Optional<String> location,
            @RequestParam Optional<Double> lat,
            @RequestParam Optional<Double> lon) {

        // Remake if time, simpler if statement and merge return for coordinates/city
        if (lat.isPresent() && lon.isPresent()) {
            String latLonLocation = lat.get() + "," + lon.get();
            return weatherService.getAndSaveWeatherByCoordinates(latLonLocation)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null))
                    .onErrorResume(e -> {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                    });
        }

        if (location.isPresent()) {
            return weatherService.getAndSaveWeatherByCity(location.get())
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
                    .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
        }
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/forecast")
    public Mono<ResponseEntity<WeatherDataForecast>> getForecastWeather(
            @RequestParam Optional<String> location,
            @RequestParam Optional<Double> lat,
            @RequestParam Optional<Double> lon) {

    }


}