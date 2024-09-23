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
import com.nick.webserviceproject.repository.CurrentWeatherRepository;
import com.nick.webserviceproject.repository.ForecastWeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {

    private final WebClient weatherWebClient;
    private final ApiService apiService;
    CurrentWeatherRepository currentWeatherRepository;
    ForecastWeatherRepository forecastWeatherRepository;

    public WeatherService(WebClient.Builder webClientBuilder, ApiService apiService, CurrentWeatherRepository currentWeatherRepository, ForecastWeatherRepository forecastWeatherRepository) {
        this.weatherWebClient = webClientBuilder
                .baseUrl("https://api.tomorrow.io/v4/weather")
                .build();
        this.apiService = apiService;
        this.currentWeatherRepository = currentWeatherRepository;
        this.forecastWeatherRepository = forecastWeatherRepository;
    }

    public Optional<WeatherDataCurrent> findWeatherByLatAndLon(double lat, double lon) {
        return currentWeatherRepository.findByLatAndLon(lat,lon);
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
                        .queryParam("timesteps", "daily")
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(WeatherDataForecastDTO.class)
                .onErrorResume(WebClientResponseException.BadRequest.class, e -> {
                    return Mono.empty();
                });
    }

    public Mono<WeatherDataCurrent> getAndSaveCurrentWeather(String location) {
        return getCurrentWeather(location)
                .map(this::mapToWeatherData)
                .doOnNext(currentWeatherRepository::save);
    }

    public Mono<List<WeatherDataForecast>> getAndSaveForecastWeather(String location) {
        return getForecastWeather(location)
                .map(this::mapToWeatherDataForecast)
                .doOnNext(forecasts -> {
                    double averageTemperature = forecasts.stream()
                            .mapToDouble(WeatherDataForecast::getTemperatureAvg)
                            .average()
                            .orElse(Double.NaN);

                    forecasts.forEach(forecast -> forecast.setWeeklyAverageTemperature(averageTemperature));
                    forecastWeatherRepository.saveAll(forecasts);
                });
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

            weatherDataForecast.setCloudBaseAvg(values.getCloudBaseAvg());
            weatherDataForecast.setCloudBaseMax(values.getCloudBaseMax());
            weatherDataForecast.setCloudBaseMin(values.getCloudBaseMin());

            weatherDataForecast.setCloudCeilingAvg(values.getCloudCeilingAvg());
            weatherDataForecast.setCloudCeilingMax(values.getCloudCeilingMax());
            weatherDataForecast.setCloudCeilingMin(values.getCloudCeilingMin());

            weatherDataForecast.setCloudCoverAvg(values.getCloudCoverAvg());
            weatherDataForecast.setCloudCoverMax(values.getCloudCoverMax());
            weatherDataForecast.setCloudCoverMin(values.getCloudCoverMin());

            weatherDataForecast.setDewPointAvg(values.getDewPointAvg());
            weatherDataForecast.setDewPointMax(values.getDewPointMax());
            weatherDataForecast.setDewPointMin(values.getDewPointMin());

            weatherDataForecast.setEvapotranspirationAvg(values.getEvapotranspirationAvg());
            weatherDataForecast.setEvapotranspirationMax(values.getEvapotranspirationMax());
            weatherDataForecast.setEvapotranspirationMin(values.getEvapotranspirationMin());
            weatherDataForecast.setEvapotranspirationSum(values.getEvapotranspirationSum());

            weatherDataForecast.setFreezingRainIntensityAvg(values.getFreezingRainIntensityAvg());
            weatherDataForecast.setFreezingRainIntensityMax(values.getFreezingRainIntensityMax());
            weatherDataForecast.setFreezingRainIntensityMin(values.getFreezingRainIntensityMin());

            weatherDataForecast.setHumidityAvg(values.getHumidityAvg());
            weatherDataForecast.setHumidityMax(values.getHumidityMax());
            weatherDataForecast.setHumidityMin(values.getHumidityMin());

            weatherDataForecast.setPrecipitationProbabilityAvg(values.getPrecipitationProbabilityAvg());
            weatherDataForecast.setPrecipitationProbabilityMax(values.getPrecipitationProbabilityMax());
            weatherDataForecast.setPrecipitationProbabilityMin(values.getPrecipitationProbabilityMin());

            weatherDataForecast.setPressureSurfaceLevelAvg(values.getPressureSurfaceLevelAvg());
            weatherDataForecast.setPressureSurfaceLevelMax(values.getPressureSurfaceLevelMax());
            weatherDataForecast.setPressureSurfaceLevelMin(values.getPressureSurfaceLevelMin());

            weatherDataForecast.setRainAccumulationAvg(values.getRainAccumulationAvg());
            weatherDataForecast.setRainAccumulationMax(values.getRainAccumulationMax());
            weatherDataForecast.setRainAccumulationMin(values.getRainAccumulationMin());
            weatherDataForecast.setRainAccumulationSum(values.getRainAccumulationSum());

            weatherDataForecast.setRainIntensityAvg(values.getRainIntensityAvg());
            weatherDataForecast.setRainIntensityMax(values.getRainIntensityMax());
            weatherDataForecast.setRainIntensityMin(values.getRainIntensityMin());

            weatherDataForecast.setSleetAccumulationAvg(values.getSleetAccumulationAvg());
            weatherDataForecast.setSleetAccumulationMax(values.getSleetAccumulationMax());
            weatherDataForecast.setSleetAccumulationMin(values.getSleetAccumulationMin());
            weatherDataForecast.setSleetAccumulationSum(values.getSleetAccumulationSum());

            weatherDataForecast.setSleetIntensityAvg(values.getSleetIntensityAvg());
            weatherDataForecast.setSleetIntensityMax(values.getSleetIntensityMax());
            weatherDataForecast.setSleetIntensityMin(values.getSleetIntensityMin());

            weatherDataForecast.setSnowAccumulationAvg(values.getSnowAccumulationAvg());
            weatherDataForecast.setSnowAccumulationMax(values.getSnowAccumulationMax());
            weatherDataForecast.setSnowAccumulationMin(values.getSnowAccumulationMin());
            weatherDataForecast.setSnowAccumulationSum(values.getSnowAccumulationSum());

            weatherDataForecast.setSnowIntensityAvg(values.getSnowIntensityAvg());
            weatherDataForecast.setSnowIntensityMax(values.getSnowIntensityMax());
            weatherDataForecast.setSnowIntensityMin(values.getSnowIntensityMin());

            weatherDataForecast.setSunriseTime(values.getSunriseTime());
            weatherDataForecast.setSunsetTime(values.getSunsetTime());

            weatherDataForecast.setTemperatureApparentAvg(values.getTemperatureApparentAvg());
            weatherDataForecast.setTemperatureApparentMax(values.getTemperatureApparentMax());
            weatherDataForecast.setTemperatureApparentMin(values.getTemperatureApparentMin());

            weatherDataForecast.setTemperatureAvg(values.getTemperatureAvg());
            weatherDataForecast.setTemperatureMax(values.getTemperatureMax());
            weatherDataForecast.setTemperatureMin(values.getTemperatureMin());

            weatherDataForecast.setUvHealthConcernAvg(values.getUvHealthConcernAvg());
            weatherDataForecast.setUvHealthConcernMax(values.getUvHealthConcernMax());
            weatherDataForecast.setUvHealthConcernMin(values.getUvHealthConcernMin());

            weatherDataForecast.setUvIndexAvg(values.getUvIndexAvg());
            weatherDataForecast.setUvIndexMax(values.getUvIndexMax());
            weatherDataForecast.setUvIndexMin(values.getUvIndexMin());

            weatherDataForecast.setVisibilityAvg(values.getVisibilityAvg());
            weatherDataForecast.setVisibilityMax(values.getVisibilityMax());
            weatherDataForecast.setVisibilityMin(values.getVisibilityMin());

            weatherDataForecast.setWeatherCodeMax(values.getWeatherCodeMax());
            weatherDataForecast.setWeatherCodeMin(values.getWeatherCodeMin());

            weatherDataForecast.setWindDirectionAvg(values.getWindDirectionAvg());

            weatherDataForecast.setWindGustAvg(values.getWindGustAvg());
            weatherDataForecast.setWindGustMax(values.getWindGustMax());
            weatherDataForecast.setWindGustMin(values.getWindGustMin());

            weatherDataForecast.setWindSpeedAvg(values.getWindSpeedAvg());
            weatherDataForecast.setWindSpeedMax(values.getWindSpeedMax());
            weatherDataForecast.setWindSpeedMin(values.getWindSpeedMin());

            weatherDataForecastList.add(weatherDataForecast);
        }
        return weatherDataForecastList;
    }


}