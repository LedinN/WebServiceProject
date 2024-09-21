package com.nick.webserviceproject.service;

import com.nick.webserviceproject.dto.WeatherDataDTO;
import com.nick.webserviceproject.model.WeatherData;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final WebClient weatherWebClient;
    private final ApiService apiService;

    public WeatherService(WebClient.Builder webClientBuilder, ApiService apiService) {
        this.weatherWebClient = webClientBuilder
                .baseUrl("https://api.tomorrow.io/v4/weather/realtime")
                .build();
        this.apiService = apiService;
    }

    public Mono<WeatherDataDTO> getCurrentWeather(String city) {
        String apiKey = apiService.getApiKey();

        return weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("location", city)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(WeatherDataDTO.class);
    }

}