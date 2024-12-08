package com.nick.webserviceproject.controller;

import com.nick.webserviceproject.dto.current.WeatherDataDTO;
import com.nick.webserviceproject.dto.forecast.WeatherDataForecastDTO;
import com.nick.webserviceproject.service.ApiService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/weather")
@Validated
public class WeatherController {

    private final WebClient weatherWebClient;
    private final RateLimiter rateLimiter;
    private final ApiService apiService;

    public WeatherController(WebClient.Builder webClientBuilder, RateLimiter rateLimiter, ApiService apiService) {
        this.weatherWebClient = webClientBuilder
                .baseUrl("https://api.tomorrow.io/v4/weather")
                .build();
        this.rateLimiter = rateLimiter;
        this.apiService = apiService;
    }

    @GetMapping("/current")
    public Mono<WeatherDataDTO> getCurrentWeather(@RequestParam String location) {
            String jwtToken = getJwtToken();
            System.out.println("INSIDE CURRENT"+getJwtToken());
            String apiKey = apiService.getApiKey();

        return weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/realtime")
                        .queryParam("location", location)
                        .queryParam("apikey", apiKey)
                        .build())
                //.header("Authorization", "Bearer " + jwtToken)
                .retrieve()
                .bodyToMono(WeatherDataDTO.class)
                .onErrorResume(WebClientResponseException.BadRequest.class, e -> {
                    System.err.println("Error fetching current weather: " + e.getMessage());
                    return Mono.empty();
                });
    }


    @GetMapping("/forecast")
    public Mono<WeatherDataForecastDTO> getForecastWeather(@RequestParam String location) {
        String jwtToken = getJwtToken(); // Extract the JWT token
        String apiKey = apiService.getApiKey();

        return weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("location", location)
                        .queryParam("timesteps", "daily")
                        .queryParam("apikey", apiKey)
                        .build())
                .header("Authorization", "Bearer " + jwtToken)
                .retrieve()
                .bodyToMono(WeatherDataForecastDTO.class)
                .onErrorResume(WebClientResponseException.BadRequest.class, e -> {
                    System.err.println("Error fetching forecast weather: " + e.getMessage());
                    return Mono.empty();
                });
    }

    private String getJwtToken() {
        String token = SecurityContextHolder.getContext().getAuthentication().getDetails().toString();
        System.out.println("Retrieved JWT Token: " + token);

        if (token == null) {
            System.out.println("NO TOKEN FOUND IN SECURITY CONTEXT");
        throw new RuntimeException("JWT token not found in SecurityContext");
        }
        return token;
    }

}