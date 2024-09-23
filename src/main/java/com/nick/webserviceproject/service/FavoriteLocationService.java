package com.nick.webserviceproject.service;

import com.nick.webserviceproject.model.FavoriteLocation;
import com.nick.webserviceproject.repository.FavoriteLocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteLocationService {

    private final FavoriteLocationRepository favoriteLocationRepository;

    public FavoriteLocationService(FavoriteLocationRepository locationRepository) {
        this.favoriteLocationRepository = locationRepository;
    }

    public FavoriteLocation addFavoriteLocation(FavoriteLocation location) {
        return favoriteLocationRepository.save(location);
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
                    existingLocation.setName(updatedLocation.getName());
                    existingLocation.setLat(updatedLocation.getLat());
                    existingLocation.setLon(updatedLocation.getLon());
                    return favoriteLocationRepository.save(existingLocation);
                });
    }

    public void deleteFavoriteLocation(Long id) {
        favoriteLocationRepository.deleteById(id);
    }
}
