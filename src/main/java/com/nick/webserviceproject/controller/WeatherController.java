package com.nick.webserviceproject.controller;

import com.nick.webserviceproject.model.current.WeatherDataCurrent;
import com.nick.webserviceproject.model.forecast.WeatherDataForecast;
import com.nick.webserviceproject.service.WeatherService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;
    private final RateLimiter rateLimiter;

    public WeatherController(WeatherService weatherService, RateLimiter rateLimiter) {
        this.weatherService = weatherService;
        this.rateLimiter = rateLimiter;
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
                .transformDeferred(RateLimiterOperator.of(rateLimiter))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> {
                    if (e instanceof RequestNotPermitted) {
                        return Mono.just(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                                .body(null));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                    }
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
                .transformDeferred(RateLimiterOperator.of(rateLimiter))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> {
                    if (e instanceof RequestNotPermitted) {
                        return Mono.just(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                                .body(null));
                    } else {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                }});
    }


}