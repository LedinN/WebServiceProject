package com.nick.webserviceproject.dto.current;

public class DataDTO {

    private String time;
    private ValuesDTO values;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ValuesDTO getValues() {
        return values;
    }

    public void setValues(ValuesDTO values) {
        this.values = values;
    }
}
