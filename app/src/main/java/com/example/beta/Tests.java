package com.example.beta;

public class Tests {
    public boolean indicator;
    public float tempC;
    public float tempF;

    public Tests(boolean indicator, float tempC, float tempF) {
        this.indicator = indicator;
        this.tempC = tempC;
        this.tempF = tempF;
    }

    public Tests() {
    }

    public boolean isIndicator() {
        return indicator;
    }

    public void setIndicator(boolean indicator) {
        this.indicator = indicator;
    }

    public float getTempC() {
        return tempC;
    }

    public void setTempC(float tempC) {
        this.tempC = tempC;
    }

    public float getTempF() {
        return tempF;
    }

    public void setTempF(float tempF) {
        this.tempF = tempF;
    }
}
