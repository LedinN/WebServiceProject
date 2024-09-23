package com.nick.webserviceproject.model.favorite;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_locations")
public class FavoriteLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Double lat;
    @Column(nullable = false)
    private Double lon;
    private String name;

    public FavoriteLocation(Double lat, Double lon, String name) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    public FavoriteLocation() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double latitude) {
        this.lat = latitude;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double longitude) {
        this.lon = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
