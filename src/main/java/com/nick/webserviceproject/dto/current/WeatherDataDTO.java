package com.nick.webserviceproject.dto.current;

import com.nick.webserviceproject.dto.common.LocationDTO;

public class WeatherDataDTO {

    private DataDTO data;
    private LocationDTO location;

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }
}
