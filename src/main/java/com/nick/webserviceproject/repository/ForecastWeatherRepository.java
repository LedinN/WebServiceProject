package com.nick.webserviceproject.repository;

import com.nick.webserviceproject.model.forecast.WeatherDataForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForecastWeatherRepository extends JpaRepository<WeatherDataForecast, Long> {
}
