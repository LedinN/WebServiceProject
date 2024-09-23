package com.nick.webserviceproject.controller;

import com.nick.webserviceproject.model.current.WeatherDataCurrent;
import com.nick.webserviceproject.model.forecast.WeatherDataForecast;
import com.nick.webserviceproject.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
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
            @RequestParam Optional<String> city,
            @RequestParam Optional<Double> lat,
            @RequestParam Optional<Double> lon) {

        String location;
        if (lat.isPresent() && lon.isPresent()) {
            location = lat.get() + "," + lon.get();
        } else if (city.isPresent()) {
            location = city.get();
        } else {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return weatherService.getAndSaveCurrentWeather(location)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }


    @GetMapping("/forecast")
    public Mono<ResponseEntity<List<WeatherDataForecast>>> getForecastWeather(
            @RequestParam Optional<String> city,
            @RequestParam Optional<Double> lat,
            @RequestParam Optional<Double> lon) {
        String location;
        if (lat.isPresent() && lon.isPresent()) {
            location = lat.get() + "," + lon.get();
        } else if (city.isPresent()) {
            location = city.get();
        } else {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return weatherService.getAndSaveForecastWeather(location)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }


}