package com.nick.webserviceproject.controller;


import com.nick.webserviceproject.model.FavoriteLocation;
import com.nick.webserviceproject.service.FavoriteLocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favorite-locations")
public class FavoriteLocationController {

    private final FavoriteLocationService favoriteLocationService;

    public FavoriteLocationController(FavoriteLocationService favoriteLocationService) {
        this.favoriteLocationService = favoriteLocationService;
    }

    @PostMapping
    public ResponseEntity<String> addFavoriteLocation(@RequestBody FavoriteLocation location) {
        FavoriteLocation savedLocation = favoriteLocationService.addFavoriteLocation(location);
        return ResponseEntity.status(201).body(savedLocation.toString());
    }

    @GetMapping
    public ResponseEntity<List<FavoriteLocation>> getAllFavoriteLocations() {
        List<FavoriteLocation> locations = favoriteLocationService.getAllFavoriteLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteLocation> getFavoriteLocationById(@PathVariable long id) {
        Optional<FavoriteLocation> location = favoriteLocationService.getFavoriteLocationById(id);
        return location.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FavoriteLocation> updateFavoriteLocation(
            @PathVariable long id,
            @RequestBody FavoriteLocation updatedLocation) {
        Optional<FavoriteLocation> location = favoriteLocationService.updateFavoriteLocation(id, updatedLocation);
        return location.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavoriteLocation(@PathVariable long id) {
        favoriteLocationService.deleteFavoriteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
