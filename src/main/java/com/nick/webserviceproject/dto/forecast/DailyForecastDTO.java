package com.nick.webserviceproject.dto.forecast;

public class DailyForecastDTO {
    private String time;
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
