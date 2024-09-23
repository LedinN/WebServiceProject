package com.nick.webserviceproject.dto.forecast;

import com.nick.webserviceproject.dto.common.LocationDTO;

public class WeatherDataForecastDTO {
    private TimelinesDTO timelinesDTO;
    private LocationDTO locationDTO;

    public TimelinesDTO getTimelinesDTO() {
        return timelinesDTO;
    }

    public void setTimelinesDTO(TimelinesDTO timelinesDTO) {
        this.timelinesDTO = timelinesDTO;
    }

    public LocationDTO getLocationDTO() {
        return locationDTO;
    }

    public void setLocationDTO(LocationDTO locationDTO) {
        this.locationDTO = locationDTO;
    }
}
