package com.nick.webserviceproject.controller;


import com.nick.webserviceproject.dto.favorite.FavoriteLocationDTO;
import com.nick.webserviceproject.model.current.WeatherDataCurrent;
import com.nick.webserviceproject.model.favorite.FavoriteLocation;
import com.nick.webserviceproject.service.FavoriteLocationService;
import com.nick.webserviceproject.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favorite-locations")
@Validated
public class FavoriteLocationController {

    private final FavoriteLocationService favoriteLocationService;
    private final WeatherService weatherService;

    public FavoriteLocationController(FavoriteLocationService favoriteLocationService, WeatherService weatherService) {
        this.favoriteLocationService = favoriteLocationService;
        this.weatherService = weatherService;
    }

    @PostMapping
    public Mono<ResponseEntity<String>> addFavoriteLocation(@Valid @RequestBody FavoriteLocationDTO locationDTO) {
        FavoriteLocation location = convertToEntity(locationDTO);
        return favoriteLocationService.addFavoriteLocation(location)
                .flatMap(savedLocation ->
                        weatherService.getAndSaveCurrentWeather(savedLocation.getLat() + "," + savedLocation.getLon())
                                .then(Mono.just(ResponseEntity.status(201)
                                        .build())
                                ));
    }

    @GetMapping
    public ResponseEntity<List<FavoriteLocation>> getAllFavoriteLocations() {
        List<FavoriteLocation> locations = favoriteLocationService.getAllFavoriteLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getFavoriteLocationById(@PathVariable long id) {
        Optional<FavoriteLocation> locationOpt = favoriteLocationService.getFavoriteLocationById(id);

        if (locationOpt.isPresent()) {
            FavoriteLocation location = locationOpt.get();
            Optional<WeatherDataCurrent> currentWeatherOpt = weatherService.findWeatherByLatAndLon(location.getLat(), location.getLon());

            if (currentWeatherOpt.isPresent()) {
                WeatherDataCurrent currentWeather = currentWeatherOpt.get();

                String response = "Location" + location.getName() +
                        "\nLatitude: " + location.getLat() +
                        "\nLongitude: " + location.getLon() +
                        "\nCurrent Weather:\n" + currentWeather.toString();
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(404).body("No weather data found");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FavoriteLocation> updateFavoriteLocation(
            @PathVariable long id,
            @Valid @RequestBody FavoriteLocationDTO updatedLocationDTO) {
        FavoriteLocation updatedLocation = convertToEntity(updatedLocationDTO);
        Optional<FavoriteLocation> location = favoriteLocationService.updateFavoriteLocation(id, updatedLocation);
        return location.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavoriteLocation(@PathVariable long id) {
        favoriteLocationService.deleteFavoriteLocation(id);
        return ResponseEntity.noContent().build();
    }

    private FavoriteLocation convertToEntity(FavoriteLocationDTO locationDTO) {
        FavoriteLocation location = new FavoriteLocation();
        location.setName(locationDTO.getName());
        location.setLat(locationDTO.getLat());
        location.setLon(locationDTO.getLon());
        return location;
    }
}
