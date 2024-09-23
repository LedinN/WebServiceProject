package com.nick.webserviceproject.dto.forecast;

import java.util.List;

public class TimelinesDTO {
    private List<DailyForecastDTO> daily;

    public List<DailyForecastDTO> getDaily() {
        return daily;
    }

    public void setDaily(List<DailyForecastDTO> daily) {
        this.daily = daily;
    }
}
