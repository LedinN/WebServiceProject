package com.nick.webserviceproject.repository;

import com.nick.webserviceproject.model.current.WeatherDataCurrent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrentWeatherRepository extends JpaRepository<WeatherDataCurrent, Long> {

    @Query("SELECT w FROM WeatherDataCurrent w WHERE w.location.lat = :lat AND w.location.lon = :lon")
    Optional<WeatherDataCurrent> findByLatAndLon(@Param("lat") Double lat, @Param("lon") Double lon);
}
