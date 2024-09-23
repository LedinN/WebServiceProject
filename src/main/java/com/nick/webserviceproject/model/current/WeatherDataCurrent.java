package com.nick.webserviceproject.model.current;

import com.nick.webserviceproject.model.common.Location;
import jakarta.persistence.*;

@Entity
@Table(name = "weather_data_current")
public class WeatherDataCurrent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Embedded
    private Data data;
    @Embedded
    private Location location;



    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Weather Data:\n" +
                "Location: " + location + "\n" +
                "Data: " + data;
    }
}
