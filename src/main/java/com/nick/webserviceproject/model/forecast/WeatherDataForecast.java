package com.nick.webserviceproject.model.forecast;

import com.nick.webserviceproject.model.common.Location;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "weather_data_forecast")
public class WeatherDataForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalDate date;

    @Embedded
    private Location location;

    private Double cloudBaseAvg;
    private Double cloudBaseMax;
    private Double cloudBaseMin;

    private Double cloudCeilingAvg;
    private Double cloudCeilingMax;
    private Double cloudCeilingMin;

    private Integer cloudCoverAvg;
    private Integer cloudCoverMax;
    private Integer cloudCoverMin;

    private Double dewPointAvg;
    private Double dewPointMax;
    private Double dewPointMin;

    private Double evapotranspirationAvg;
    private Double evapotranspirationMax;
    private Double evapotranspirationMin;
    private Double evapotranspirationSum;

    private Integer freezingRainIntensityAvg;
    private Integer freezingRainIntensityMax;
    private Integer freezingRainIntensityMin;

    private Integer humidityAvg;
    private Integer humidityMax;
    private Integer humidityMin;

    private Double iceAccumulationAvg;
    private Double iceAccumulationMax;
    private Double iceAccumulationMin;
    private Double iceAccumulationSum;

    private Double iceAccumulationLweAvg;
    private Double iceAccumulationLweMax;
    private Double iceAccumulationLweMin;
    private Double iceAccumulationLweSum;

    private String moonriseTime;
    private String moonsetTime;

    private Integer precipitationProbabilityAvg;
    private Integer precipitationProbabilityMax;
    private Integer precipitationProbabilityMin;

    private Double pressureSurfaceLevelAvg;
    private Double pressureSurfaceLevelMax;
    private Double pressureSurfaceLevelMin;

    private Double rainAccumulationAvg;
    private Double rainAccumulationMax;
    private Double rainAccumulationMin;
    private Double rainAccumulationSum;

    private Double rainAccumulationLweAvg;
    private Double rainAccumulationLweMax;
    private Double rainAccumulationLweMin;
    private Double rainAccumulationLweSum;

    private Integer rainIntensityAvg;
    private Integer rainIntensityMax;
    private Integer rainIntensityMin;

    private Double sleetAccumulationAvg;
    private Double sleetAccumulationMax;
    private Double sleetAccumulationMin;
    private Double sleetAccumulationSum;

    private Double sleetAccumulationLweAvg;
    private Double sleetAccumulationLweMax;
    private Double sleetAccumulationLweMin;
    private Double sleetAccumulationLweSum;

    private Integer sleetIntensityAvg;
    private Integer sleetIntensityMax;
    private Integer sleetIntensityMin;

    private Double snowAccumulationAvg;
    private Double snowAccumulationMax;
    private Double snowAccumulationMin;
    private Double snowAccumulationSum;

    private Double snowAccumulationLweAvg;
    private Double snowAccumulationLweMax;
    private Double snowAccumulationLweMin;
    private Double snowAccumulationLweSum;

    private Integer snowIntensityAvg;
    private Integer snowIntensityMax;
    private Integer snowIntensityMin;

    private String sunriseTime;
    private String sunsetTime;

    private Double temperatureApparentAvg;
    private Double temperatureApparentMax;
    private Double temperatureApparentMin;

    private Double temperatureAvg;
    private Double temperatureMax;
    private Double temperatureMin;

    private Integer uvHealthConcernAvg;
    private Integer uvHealthConcernMax;
    private Integer uvHealthConcernMin;

    private Integer uvIndexAvg;
    private Integer uvIndexMax;
    private Integer uvIndexMin;

    private Integer visibilityAvg;
    private Integer visibilityMax;
    private Integer visibilityMin;

    private Integer weatherCodeMax;
    private Integer weatherCodeMin;

    private Double windDirectionAvg;

    private Double windGustAvg;
    private Double windGustMax;
    private Double windGustMin;

    private Double windSpeedAvg;
    private Double windSpeedMax;
    private Double windSpeedMin;

    private Double weeklyAverageTemperature;

    public Double getWeeklyAverageTemperature() {
        return weeklyAverageTemperature;
    }

    public void setWeeklyAverageTemperature(Double weeklyAverageTemperature) {
        this.weeklyAverageTemperature = weeklyAverageTemperature;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getCloudBaseAvg() {
        return cloudBaseAvg;
    }

    public void setCloudBaseAvg(Double cloudBaseAvg) {
        this.cloudBaseAvg = cloudBaseAvg;
    }

    public Double getCloudBaseMax() {
        return cloudBaseMax;
    }

    public void setCloudBaseMax(Double cloudBaseMax) {
        this.cloudBaseMax = cloudBaseMax;
    }

    public Double getCloudBaseMin() {
        return cloudBaseMin;
    }

    public void setCloudBaseMin(Double cloudBaseMin) {
        this.cloudBaseMin = cloudBaseMin;
    }

    public Double getCloudCeilingAvg() {
        return cloudCeilingAvg;
    }

    public void setCloudCeilingAvg(Double cloudCeilingAvg) {
        this.cloudCeilingAvg = cloudCeilingAvg;
    }

    public Double getCloudCeilingMax() {
        return cloudCeilingMax;
    }

    public void setCloudCeilingMax(Double cloudCeilingMax) {
        this.cloudCeilingMax = cloudCeilingMax;
    }

    public Double getCloudCeilingMin() {
        return cloudCeilingMin;
    }

    public void setCloudCeilingMin(Double cloudCeilingMin) {
        this.cloudCeilingMin = cloudCeilingMin;
    }

    public Integer getCloudCoverAvg() {
        return cloudCoverAvg;
    }

    public void setCloudCoverAvg(Integer cloudCoverAvg) {
        this.cloudCoverAvg = cloudCoverAvg;
    }

    public Integer getCloudCoverMax() {
        return cloudCoverMax;
    }

    public void setCloudCoverMax(Integer cloudCoverMax) {
        this.cloudCoverMax = cloudCoverMax;
    }

    public Integer getCloudCoverMin() {
        return cloudCoverMin;
    }

    public void setCloudCoverMin(Integer cloudCoverMin) {
        this.cloudCoverMin = cloudCoverMin;
    }

    public Double getDewPointAvg() {
        return dewPointAvg;
    }

    public void setDewPointAvg(Double dewPointAvg) {
        this.dewPointAvg = dewPointAvg;
    }

    public Double getDewPointMax() {
        return dewPointMax;
    }

    public void setDewPointMax(Double dewPointMax) {
        this.dewPointMax = dewPointMax;
    }

    public Double getDewPointMin() {
        return dewPointMin;
    }

    public void setDewPointMin(Double dewPointMin) {
        this.dewPointMin = dewPointMin;
    }

    public Double getEvapotranspirationAvg() {
        return evapotranspirationAvg;
    }

    public void setEvapotranspirationAvg(Double evapotranspirationAvg) {
        this.evapotranspirationAvg = evapotranspirationAvg;
    }

    public Double getEvapotranspirationMax() {
        return evapotranspirationMax;
    }

    public void setEvapotranspirationMax(Double evapotranspirationMax) {
        this.evapotranspirationMax = evapotranspirationMax;
    }

    public Double getEvapotranspirationMin() {
        return evapotranspirationMin;
    }

    public void setEvapotranspirationMin(Double evapotranspirationMin) {
        this.evapotranspirationMin = evapotranspirationMin;
    }

    public Double getEvapotranspirationSum() {
        return evapotranspirationSum;
    }

    public void setEvapotranspirationSum(Double evapotranspirationSum) {
        this.evapotranspirationSum = evapotranspirationSum;
    }

    public Integer getFreezingRainIntensityAvg() {
        return freezingRainIntensityAvg;
    }

    public void setFreezingRainIntensityAvg(Integer freezingRainIntensityAvg) {
        this.freezingRainIntensityAvg = freezingRainIntensityAvg;
    }

    public Integer getFreezingRainIntensityMax() {
        return freezingRainIntensityMax;
    }

    public void setFreezingRainIntensityMax(Integer freezingRainIntensityMax) {
        this.freezingRainIntensityMax = freezingRainIntensityMax;
    }

    public Integer getFreezingRainIntensityMin() {
        return freezingRainIntensityMin;
    }

    public void setFreezingRainIntensityMin(Integer freezingRainIntensityMin) {
        this.freezingRainIntensityMin = freezingRainIntensityMin;
    }

    public Integer getHumidityAvg() {
        return humidityAvg;
    }

    public void setHumidityAvg(Integer humidityAvg) {
        this.humidityAvg = humidityAvg;
    }

    public Integer getHumidityMax() {
        return humidityMax;
    }

    public void setHumidityMax(Integer humidityMax) {
        this.humidityMax = humidityMax;
    }

    public Integer getHumidityMin() {
        return humidityMin;
    }

    public void setHumidityMin(Integer humidityMin) {
        this.humidityMin = humidityMin;
    }

    public Double getIceAccumulationAvg() {
        return iceAccumulationAvg;
    }

    public void setIceAccumulationAvg(Double iceAccumulationAvg) {
        this.iceAccumulationAvg = iceAccumulationAvg;
    }

    public Double getIceAccumulationMax() {
        return iceAccumulationMax;
    }

    public void setIceAccumulationMax(Double iceAccumulationMax) {
        this.iceAccumulationMax = iceAccumulationMax;
    }

    public Double getIceAccumulationMin() {
        return iceAccumulationMin;
    }

    public void setIceAccumulationMin(Double iceAccumulationMin) {
        this.iceAccumulationMin = iceAccumulationMin;
    }

    public Double getIceAccumulationSum() {
        return iceAccumulationSum;
    }

    public void setIceAccumulationSum(Double iceAccumulationSum) {
        this.iceAccumulationSum = iceAccumulationSum;
    }

    public Double getIceAccumulationLweAvg() {
        return iceAccumulationLweAvg;
    }

    public void setIceAccumulationLweAvg(Double iceAccumulationLweAvg) {
        this.iceAccumulationLweAvg = iceAccumulationLweAvg;
    }

    public Double getIceAccumulationLweMax() {
        return iceAccumulationLweMax;
    }

    public void setIceAccumulationLweMax(Double iceAccumulationLweMax) {
        this.iceAccumulationLweMax = iceAccumulationLweMax;
    }

    public Double getIceAccumulationLweMin() {
        return iceAccumulationLweMin;
    }

    public void setIceAccumulationLweMin(Double iceAccumulationLweMin) {
        this.iceAccumulationLweMin = iceAccumulationLweMin;
    }

    public Double getIceAccumulationLweSum() {
        return iceAccumulationLweSum;
    }

    public void setIceAccumulationLweSum(Double iceAccumulationLweSum) {
        this.iceAccumulationLweSum = iceAccumulationLweSum;
    }

    public String getMoonriseTime() {
        return moonriseTime;
    }

    public void setMoonriseTime(String moonriseTime) {
        this.moonriseTime = moonriseTime;
    }

    public String getMoonsetTime() {
        return moonsetTime;
    }

    public void setMoonsetTime(String moonsetTime) {
        this.moonsetTime = moonsetTime;
    }

    public Integer getPrecipitationProbabilityAvg() {
        return precipitationProbabilityAvg;
    }

    public void setPrecipitationProbabilityAvg(Integer precipitationProbabilityAvg) {
        this.precipitationProbabilityAvg = precipitationProbabilityAvg;
    }

    public Integer getPrecipitationProbabilityMax() {
        return precipitationProbabilityMax;
    }

    public void setPrecipitationProbabilityMax(Integer precipitationProbabilityMax) {
        this.precipitationProbabilityMax = precipitationProbabilityMax;
    }

    public Integer getPrecipitationProbabilityMin() {
        return precipitationProbabilityMin;
    }

    public void setPrecipitationProbabilityMin(Integer precipitationProbabilityMin) {
        this.precipitationProbabilityMin = precipitationProbabilityMin;
    }

    public Double getPressureSurfaceLevelAvg() {
        return pressureSurfaceLevelAvg;
    }

    public void setPressureSurfaceLevelAvg(Double pressureSurfaceLevelAvg) {
        this.pressureSurfaceLevelAvg = pressureSurfaceLevelAvg;
    }

    public Double getPressureSurfaceLevelMax() {
        return pressureSurfaceLevelMax;
    }

    public void setPressureSurfaceLevelMax(Double pressureSurfaceLevelMax) {
        this.pressureSurfaceLevelMax = pressureSurfaceLevelMax;
    }

    public Double getPressureSurfaceLevelMin() {
        return pressureSurfaceLevelMin;
    }

    public void setPressureSurfaceLevelMin(Double pressureSurfaceLevelMin) {
        this.pressureSurfaceLevelMin = pressureSurfaceLevelMin;
    }

    public Double getRainAccumulationAvg() {
        return rainAccumulationAvg;
    }

    public void setRainAccumulationAvg(Double rainAccumulationAvg) {
        this.rainAccumulationAvg = rainAccumulationAvg;
    }

    public Double getRainAccumulationMax() {
        return rainAccumulationMax;
    }

    public void setRainAccumulationMax(Double rainAccumulationMax) {
        this.rainAccumulationMax = rainAccumulationMax;
    }

    public Double getRainAccumulationMin() {
        return rainAccumulationMin;
    }

    public void setRainAccumulationMin(Double rainAccumulationMin) {
        this.rainAccumulationMin = rainAccumulationMin;
    }

    public Double getRainAccumulationSum() {
        return rainAccumulationSum;
    }

    public void setRainAccumulationSum(Double rainAccumulationSum) {
        this.rainAccumulationSum = rainAccumulationSum;
    }

    public Double getRainAccumulationLweAvg() {
        return rainAccumulationLweAvg;
    }

    public void setRainAccumulationLweAvg(Double rainAccumulationLweAvg) {
        this.rainAccumulationLweAvg = rainAccumulationLweAvg;
    }

    public Double getRainAccumulationLweMax() {
        return rainAccumulationLweMax;
    }

    public void setRainAccumulationLweMax(Double rainAccumulationLweMax) {
        this.rainAccumulationLweMax = rainAccumulationLweMax;
    }

    public Double getRainAccumulationLweMin() {
        return rainAccumulationLweMin;
    }

    public void setRainAccumulationLweMin(Double rainAccumulationLweMin) {
        this.rainAccumulationLweMin = rainAccumulationLweMin;
    }

    public Double getRainAccumulationLweSum() {
        return rainAccumulationLweSum;
    }

    public void setRainAccumulationLweSum(Double rainAccumulationLweSum) {
        this.rainAccumulationLweSum = rainAccumulationLweSum;
    }

    public Integer getRainIntensityAvg() {
        return rainIntensityAvg;
    }

    public void setRainIntensityAvg(Integer rainIntensityAvg) {
        this.rainIntensityAvg = rainIntensityAvg;
    }

    public Integer getRainIntensityMax() {
        return rainIntensityMax;
    }

    public void setRainIntensityMax(Integer rainIntensityMax) {
        this.rainIntensityMax = rainIntensityMax;
    }

    public Integer getRainIntensityMin() {
        return rainIntensityMin;
    }

    public void setRainIntensityMin(Integer rainIntensityMin) {
        this.rainIntensityMin = rainIntensityMin;
    }

    public Double getSleetAccumulationAvg() {
        return sleetAccumulationAvg;
    }

    public void setSleetAccumulationAvg(Double sleetAccumulationAvg) {
        this.sleetAccumulationAvg = sleetAccumulationAvg;
    }

    public Double getSleetAccumulationMax() {
        return sleetAccumulationMax;
    }

    public void setSleetAccumulationMax(Double sleetAccumulationMax) {
        this.sleetAccumulationMax = sleetAccumulationMax;
    }

    public Double getSleetAccumulationMin() {
        return sleetAccumulationMin;
    }

    public void setSleetAccumulationMin(Double sleetAccumulationMin) {
        this.sleetAccumulationMin = sleetAccumulationMin;
    }

    public Double getSleetAccumulationSum() {
        return sleetAccumulationSum;
    }

    public void setSleetAccumulationSum(Double sleetAccumulationSum) {
        this.sleetAccumulationSum = sleetAccumulationSum;
    }

    public Double getSleetAccumulationLweAvg() {
        return sleetAccumulationLweAvg;
    }

    public void setSleetAccumulationLweAvg(Double sleetAccumulationLweAvg) {
        this.sleetAccumulationLweAvg = sleetAccumulationLweAvg;
    }

    public Double getSleetAccumulationLweMax() {
        return sleetAccumulationLweMax;
    }

    public void setSleetAccumulationLweMax(Double sleetAccumulationLweMax) {
        this.sleetAccumulationLweMax = sleetAccumulationLweMax;
    }

    public Double getSleetAccumulationLweMin() {
        return sleetAccumulationLweMin;
    }

    public void setSleetAccumulationLweMin(Double sleetAccumulationLweMin) {
        this.sleetAccumulationLweMin = sleetAccumulationLweMin;
    }

    public Double getSleetAccumulationLweSum() {
        return sleetAccumulationLweSum;
    }

    public void setSleetAccumulationLweSum(Double sleetAccumulationLweSum) {
        this.sleetAccumulationLweSum = sleetAccumulationLweSum;
    }

    public Integer getSleetIntensityAvg() {
        return sleetIntensityAvg;
    }

    public void setSleetIntensityAvg(Integer sleetIntensityAvg) {
        this.sleetIntensityAvg = sleetIntensityAvg;
    }

    public Integer getSleetIntensityMax() {
        return sleetIntensityMax;
    }

    public void setSleetIntensityMax(Integer sleetIntensityMax) {
        this.sleetIntensityMax = sleetIntensityMax;
    }

    public Integer getSleetIntensityMin() {
        return sleetIntensityMin;
    }

    public void setSleetIntensityMin(Integer sleetIntensityMin) {
        this.sleetIntensityMin = sleetIntensityMin;
    }

    public Double getSnowAccumulationAvg() {
        return snowAccumulationAvg;
    }

    public void setSnowAccumulationAvg(Double snowAccumulationAvg) {
        this.snowAccumulationAvg = snowAccumulationAvg;
    }

    public Double getSnowAccumulationMax() {
        return snowAccumulationMax;
    }

    public void setSnowAccumulationMax(Double snowAccumulationMax) {
        this.snowAccumulationMax = snowAccumulationMax;
    }

    public Double getSnowAccumulationMin() {
        return snowAccumulationMin;
    }

    public void setSnowAccumulationMin(Double snowAccumulationMin) {
        this.snowAccumulationMin = snowAccumulationMin;
    }

    public Double getSnowAccumulationSum() {
        return snowAccumulationSum;
    }

    public void setSnowAccumulationSum(Double snowAccumulationSum) {
        this.snowAccumulationSum = snowAccumulationSum;
    }

    public Double getSnowAccumulationLweAvg() {
        return snowAccumulationLweAvg;
    }

    public void setSnowAccumulationLweAvg(Double snowAccumulationLweAvg) {
        this.snowAccumulationLweAvg = snowAccumulationLweAvg;
    }

    public Double getSnowAccumulationLweMax() {
        return snowAccumulationLweMax;
    }

    public void setSnowAccumulationLweMax(Double snowAccumulationLweMax) {
        this.snowAccumulationLweMax = snowAccumulationLweMax;
    }

    public Double getSnowAccumulationLweMin() {
        return snowAccumulationLweMin;
    }

    public void setSnowAccumulationLweMin(Double snowAccumulationLweMin) {
        this.snowAccumulationLweMin = snowAccumulationLweMin;
    }

    public Double getSnowAccumulationLweSum() {
        return snowAccumulationLweSum;
    }

    public void setSnowAccumulationLweSum(Double snowAccumulationLweSum) {
        this.snowAccumulationLweSum = snowAccumulationLweSum;
    }

    public Integer getSnowIntensityAvg() {
        return snowIntensityAvg;
    }

    public void setSnowIntensityAvg(Integer snowIntensityAvg) {
        this.snowIntensityAvg = snowIntensityAvg;
    }

    public Integer getSnowIntensityMax() {
        return snowIntensityMax;
    }

    public void setSnowIntensityMax(Integer snowIntensityMax) {
        this.snowIntensityMax = snowIntensityMax;
    }

    public Integer getSnowIntensityMin() {
        return snowIntensityMin;
    }

    public void setSnowIntensityMin(Integer snowIntensityMin) {
        this.snowIntensityMin = snowIntensityMin;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public Double getTemperatureApparentAvg() {
        return temperatureApparentAvg;
    }

    public void setTemperatureApparentAvg(Double temperatureApparentAvg) {
        this.temperatureApparentAvg = temperatureApparentAvg;
    }

    public Double getTemperatureApparentMax() {
        return temperatureApparentMax;
    }

    public void setTemperatureApparentMax(Double temperatureApparentMax) {
        this.temperatureApparentMax = temperatureApparentMax;
    }

    public Double getTemperatureApparentMin() {
        return temperatureApparentMin;
    }

    public void setTemperatureApparentMin(Double temperatureApparentMin) {
        this.temperatureApparentMin = temperatureApparentMin;
    }

    public Double getTemperatureAvg() {
        return temperatureAvg;
    }

    public void setTemperatureAvg(Double temperatureAvg) {
        this.temperatureAvg = temperatureAvg;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Integer getUvHealthConcernAvg() {
        return uvHealthConcernAvg;
    }

    public void setUvHealthConcernAvg(Integer uvHealthConcernAvg) {
        this.uvHealthConcernAvg = uvHealthConcernAvg;
    }

    public Integer getUvHealthConcernMax() {
        return uvHealthConcernMax;
    }

    public void setUvHealthConcernMax(Integer uvHealthConcernMax) {
        this.uvHealthConcernMax = uvHealthConcernMax;
    }

    public Integer getUvHealthConcernMin() {
        return uvHealthConcernMin;
    }

    public void setUvHealthConcernMin(Integer uvHealthConcernMin) {
        this.uvHealthConcernMin = uvHealthConcernMin;
    }

    public Integer getUvIndexAvg() {
        return uvIndexAvg;
    }

    public void setUvIndexAvg(Integer uvIndexAvg) {
        this.uvIndexAvg = uvIndexAvg;
    }

    public Integer getUvIndexMax() {
        return uvIndexMax;
    }

    public void setUvIndexMax(Integer uvIndexMax) {
        this.uvIndexMax = uvIndexMax;
    }

    public Integer getUvIndexMin() {
        return uvIndexMin;
    }

    public void setUvIndexMin(Integer uvIndexMin) {
        this.uvIndexMin = uvIndexMin;
    }

    public Integer getVisibilityAvg() {
        return visibilityAvg;
    }

    public void setVisibilityAvg(Integer visibilityAvg) {
        this.visibilityAvg = visibilityAvg;
    }

    public Integer getVisibilityMax() {
        return visibilityMax;
    }

    public void setVisibilityMax(Integer visibilityMax) {
        this.visibilityMax = visibilityMax;
    }

    public Integer getVisibilityMin() {
        return visibilityMin;
    }

    public void setVisibilityMin(Integer visibilityMin) {
        this.visibilityMin = visibilityMin;
    }

    public Integer getWeatherCodeMax() {
        return weatherCodeMax;
    }

    public void setWeatherCodeMax(Integer weatherCodeMax) {
        this.weatherCodeMax = weatherCodeMax;
    }

    public Integer getWeatherCodeMin() {
        return weatherCodeMin;
    }

    public void setWeatherCodeMin(Integer weatherCodeMin) {
        this.weatherCodeMin = weatherCodeMin;
    }

    public Double getWindDirectionAvg() {
        return windDirectionAvg;
    }

    public void setWindDirectionAvg(Double windDirectionAvg) {
        this.windDirectionAvg = windDirectionAvg;
    }

    public Double getWindGustAvg() {
        return windGustAvg;
    }

    public void setWindGustAvg(Double windGustAvg) {
        this.windGustAvg = windGustAvg;
    }

    public Double getWindGustMax() {
        return windGustMax;
    }

    public void setWindGustMax(Double windGustMax) {
        this.windGustMax = windGustMax;
    }

    public Double getWindGustMin() {
        return windGustMin;
    }

    public void setWindGustMin(Double windGustMin) {
        this.windGustMin = windGustMin;
    }

    public Double getWindSpeedAvg() {
        return windSpeedAvg;
    }

    public void setWindSpeedAvg(Double windSpeedAvg) {
        this.windSpeedAvg = windSpeedAvg;
    }

    public Double getWindSpeedMax() {
        return windSpeedMax;
    }

    public void setWindSpeedMax(Double windSpeedMax) {
        this.windSpeedMax = windSpeedMax;
    }

    public Double getWindSpeedMin() {
        return windSpeedMin;
    }

    public void setWindSpeedMin(Double windSpeedMin) {
        this.windSpeedMin = windSpeedMin;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
