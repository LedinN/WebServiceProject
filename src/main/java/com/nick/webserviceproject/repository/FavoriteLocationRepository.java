package com.nick.webserviceproject.repository;

import com.nick.webserviceproject.model.FavoriteLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteLocationRepository extends JpaRepository<FavoriteLocation, Long> {
}
