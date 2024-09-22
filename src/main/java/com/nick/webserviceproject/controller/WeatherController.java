package com.nick.webserviceproject.controller;

import com.nick.webserviceproject.model.WeatherData;
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

    @GetMapping()
    public Mono<ResponseEntity<WeatherData>> getWeather(
            @RequestParam Optional<String> location,
            @RequestParam Optional<Double> lat,
            @RequestParam Optional<Double> lon) {

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


}