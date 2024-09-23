package com.nick.webserviceproject.repository;

import com.nick.webserviceproject.model.current.WeatherDataCurrent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentWeatherRepository extends JpaRepository<WeatherDataCurrent, Long> {
}
