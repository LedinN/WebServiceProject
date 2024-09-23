package com.nick.webserviceproject.model.current;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class Data {
    private String time;
    @Embedded
    private Values values;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Values getValues() {
        return values;
    }

    public void setValues(Values values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Time: " + time + "\n" +
                "Values: " + values.toString();
    }

}
