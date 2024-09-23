package com.nick.webserviceproject.dto.favorite;

import jakarta.validation.constraints.*;

public class FavoriteLocationDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 40, message = "City name must be 40 characters or shorter" )
    @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Invalid city name")
    private String name;

    @Min(value = -90, message = "Latitude must be between -90 and 90")
    @Max(value = 90, message = "Latitude must be between -90 and 90")
    private Double lat;

    @Min(value = -180, message = "Longitude must be between -180 and 180")
    @Max(value = 180, message = "Longitude must be between -180 and 180")
    private Double lon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
