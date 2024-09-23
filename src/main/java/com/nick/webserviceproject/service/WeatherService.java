package com.nick.webserviceproject.service;

import com.nick.webserviceproject.dto.current.WeatherDataDTO;
import com.nick.webserviceproject.dto.forecast.DailyForecastDTO;
import com.nick.webserviceproject.dto.forecast.DailyValuesDTO;
import com.nick.webserviceproject.dto.forecast.WeatherDataForecastDTO;
import com.nick.webserviceproject.model.current.Data;
import com.nick.webserviceproject.model.common.Location;
import com.nick.webserviceproject.model.current.Values;
import com.nick.webserviceproject.model.current.WeatherDataCurrent;
import com.nick.webserviceproject.model.forecast.WeatherDataForecast;
import com.nick.webserviceproject.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final WebClient weatherWebClient;
    private final ApiService apiService;
    WeatherRepository weatherRepository;

    public WeatherService(WebClient.Builder webClientBuilder, ApiService apiService, WeatherRepository weatherRepository) {
        this.weatherWebClient = webClientBuilder
                .baseUrl("https://api.tomorrow.io/v4/weather")
                .build();
        this.apiService = apiService;
        this.weatherRepository = weatherRepository;
    }

    public Mono<WeatherDataDTO> getCurrentWeather(String location) {
        String apiKey = apiService.getApiKey();

        return weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/realtime")
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

    public Mono<WeatherDataForecastDTO> getForecastWeather(String location) {
        String apiKey = apiService.getApiKey();

        return weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("location", location)
                        .queryParam("timesteps","daily")
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(WeatherDataForecastDTO.class)
                .onErrorResume(WebClientResponseException.BadRequest.class, e -> {
                    return Mono.empty();
                });
    }

    public Mono<WeatherDataCurrent> getAndSaveWeatherByCity(String city) {
        return getCurrentWeather(city)
                .map(this::mapToWeatherData)
                .doOnNext(weatherRepository::save);
    }

    public Mono<WeatherDataCurrent> getAndSaveWeatherByCoordinates(String latLonLocation) {
        return getCurrentWeather(latLonLocation)
                .map(this::mapToWeatherData)
                .doOnNext(weatherRepository::save);
    }

    private WeatherDataCurrent mapToWeatherData(WeatherDataDTO weatherDataDTO) {
        WeatherDataCurrent weatherDataCurrent = new WeatherDataCurrent();

        Location location = new Location();
        location.setLat(weatherDataDTO.getLocation().getLat());
        location.setLon(weatherDataDTO.getLocation().getLon());
        location.setName(weatherDataDTO.getLocation().getName());
        weatherDataCurrent.setLocation(location);

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
        weatherDataCurrent.setData(data);

        return weatherDataCurrent;
    }

    private List<WeatherDataForecast> mapToWeatherDataForecast(WeatherDataForecastDTO weatherDataForecastDTO) {
        List<WeatherDataForecast> weatherDataForecastList = new ArrayList<>();

        for (DailyForecastDTO dailyForecastDTO : weatherDataForecastDTO.getTimelinesDTO().getDaily()) {
            WeatherDataForecast weatherDataForecast = new WeatherDataForecast();

            weatherDataForecast.setDate(LocalDate.parse(dailyForecastDTO.getTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            DailyValuesDTO values = dailyForecastDTO.getValuesDTO();

            Location location = new Location();
            location.setLat(weatherDataForecastDTO.getLocationDTO().getLat());
            location.setLon(weatherDataForecastDTO.getLocationDTO().getLon());
            weatherDataForecast.setLocation(location);

            weatherDataForecastList.add(weatherDataForecast);
        }
        return weatherDataForecastList;
    }


}