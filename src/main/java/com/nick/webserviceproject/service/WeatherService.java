package com.nick.webserviceproject.service;

import com.nick.webserviceproject.dto.WeatherDataDTO;
import com.nick.webserviceproject.model.Data;
import com.nick.webserviceproject.model.Location;
import com.nick.webserviceproject.model.Values;
import com.nick.webserviceproject.model.WeatherData;
import com.nick.webserviceproject.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final WebClient weatherWebClient;
    private final ApiService apiService;
    WeatherRepository weatherRepository;

    public WeatherService(WebClient.Builder webClientBuilder, ApiService apiService, WeatherRepository weatherRepository) {
        this.weatherWebClient = webClientBuilder
                .baseUrl("https://api.tomorrow.io/v4/weather/realtime")
                .build();
        this.apiService = apiService;
        this.weatherRepository = weatherRepository;
    }

    public Mono<WeatherDataDTO> getCurrentWeather(String location) {
        String apiKey = apiService.getApiKey();

        return weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("location", location)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(WeatherDataDTO.class)
                .onErrorResume(WebClientResponseException.BadRequest.class, e -> {
                   return Mono.empty();
                });
    }

    public Mono<WeatherDataDTO> getCurrentWeather(Double lat, Double lon) {
        String apiKey = apiService.getApiKey();

        return weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(WeatherDataDTO.class)
                .onErrorResume(WebClientResponseException.BadRequest.class, e -> {
                    return Mono.empty();
                });
    }

    public Mono<WeatherData> getAndSaveWeatherByCity(String city) {
        return getCurrentWeather(city)
                .map(this::mapToWeatherData)
                .doOnNext(weatherRepository::save);
    }

    public Mono<WeatherData> getAndSaveWeatherByCoordinates(String latLonLocation) {
        return getCurrentWeather(latLonLocation)
                .map(this::mapToWeatherData)
                .doOnNext(weatherRepository::save);
    }

    private WeatherData mapToWeatherData(WeatherDataDTO weatherDataDTO) {
        WeatherData weatherData = new WeatherData();

        Location location = new Location();
        location.setLat(weatherDataDTO.getLocation().getLat());
        location.setLon(weatherDataDTO.getLocation().getLon());
        location.setName(weatherDataDTO.getLocation().getName());
        weatherData.setLocation(location);

        Data data = new Data();
        data.setTime(weatherDataDTO.getData().getTime());

        Values values = new Values();
        values.setTemperature(weatherDataDTO.getData().getValues().getTemperature());
        values.setHumidity(weatherDataDTO.getData().getValues().getHumidity());
        values.setWindSpeed(weatherDataDTO.getData().getValues().getWindSpeed());
        values.setCloudCover(weatherDataDTO.getData().getValues().getCloudCover());
        values.setDewPoint(weatherDataDTO.getData().getValues().getDewPoint());
        values.setPrecipitationProbability(weatherDataDTO.getData().getValues().getPrecipitationProbability());
        values.setPressureSurfaceLevel(weatherDataDTO.getData().getValues().getPressureSurfaceLevel());
        values.setVisibility(weatherDataDTO.getData().getValues().getVisibility());
        values.setWindDirection(weatherDataDTO.getData().getValues().getWindDirection());
        values.setWindGust(weatherDataDTO.getData().getValues().getWindGust());

        data.setValues(values);
        weatherData.setData(data);

        return weatherData;
    }


}