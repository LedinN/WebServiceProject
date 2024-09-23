package com.nick.webserviceproject.service;

import com.nick.webserviceproject.model.favorite.FavoriteLocation;
import com.nick.webserviceproject.repository.FavoriteLocationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteLocationService {

    private final FavoriteLocationRepository favoriteLocationRepository;

    public FavoriteLocationService(FavoriteLocationRepository locationRepository) {
        this.favoriteLocationRepository = locationRepository;
    }

    public Mono<FavoriteLocation> addFavoriteLocation(FavoriteLocation location) {
        return Mono.just(favoriteLocationRepository.save(location));
    }

    public List<FavoriteLocation> getAllFavoriteLocations() {
        return favoriteLocationRepository.findAll();
    }

    public Optional<FavoriteLocation> getFavoriteLocationById(Long id) {
        return favoriteLocationRepository.findById(id);
    }

    public Optional<FavoriteLocation> updateFavoriteLocation(Long id, FavoriteLocation updatedLocation) {
        return favoriteLocationRepository.findById(id)
                .map(existingLocation -> {
                    if (updatedLocation.getName() != null) {
                        existingLocation.setName(updatedLocation.getName());
                    }
                    if (updatedLocation.getLat() != null) {
                        existingLocation.setLat(updatedLocation.getLat());
                    }
                    if (updatedLocation.getLon() != null) {
                        existingLocation.setLon(updatedLocation.getLon());
                    }
                    return favoriteLocationRepository.save(existingLocation);
                });
    }

    public void deleteFavoriteLocation(Long id) {
        favoriteLocationRepository.deleteById(id);
    }
}
