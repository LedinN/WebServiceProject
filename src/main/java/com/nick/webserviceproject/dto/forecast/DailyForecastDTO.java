package com.nick.webserviceproject.dto.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyForecastDTO {
    private String time;
    @JsonProperty("values")
    private DailyValuesDTO valuesDTO;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DailyValuesDTO getValuesDTO() {
        return valuesDTO;
    }

    public void setValuesDTO(DailyValuesDTO valuesDTO) {
        this.valuesDTO = valuesDTO;
    }
}
