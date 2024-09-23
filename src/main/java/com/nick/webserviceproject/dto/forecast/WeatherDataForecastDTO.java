package com.nick.webserviceproject.dto.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nick.webserviceproject.dto.common.LocationDTO;

public class WeatherDataForecastDTO {

    @JsonProperty("timelines")
    private TimelinesDTO timelinesDTO;
    @JsonProperty("location")
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
