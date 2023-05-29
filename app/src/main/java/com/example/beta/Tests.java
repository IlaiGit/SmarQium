package com.example.beta;

public class Tests {
    public boolean indicator;
    public double tempC;
    public double tempF;

    public double waterClarity;
    public double waterHeight;
    public double waterPH;
    public boolean activateFeeder;

    public Tests(){
    }

    public boolean isIndicator() {
        return indicator;
    }

    public void setIndicator(boolean indicator) {
        this.indicator = indicator;
    }

    public double getTempC() {
        return tempC;
    }

    public void setTempC(double tempC) {
        this.tempC = tempC;
    }

    public double getTempF() {
        return tempF;
    }

    public void setTempF(double tempF) {
        this.tempF = tempF;
    }

    public double getWaterClarity() {
        return waterClarity;
    }

    public void setWaterClarity(double waterClarity) {
        this.waterClarity = waterClarity;
    }

    public double getWaterHeight() {
        return waterHeight;
    }

    public void setWaterHeight(double waterHeight) {
        this.waterHeight = waterHeight;
    }

    public double getWaterPH() {
        return waterPH;
    }

    public void setWaterPH(double waterPH) {
        this.waterPH = waterPH;
    }

    public boolean isActivateFeeder() {
        return activateFeeder;
    }

    public void setActivateFeeder(boolean activateFeeder) {
        this.activateFeeder = activateFeeder;
    }

    public Tests(boolean indicator, double tempC, double tempF, double waterClarity, double waterHeight, double waterPH, boolean activateFeeder ){
        this.indicator = indicator;
        this.tempC = tempC;
        this.tempF = tempF;
        this.waterClarity = waterClarity;
        this.waterHeight = waterHeight;
        this.waterPH = waterPH;
        this.activateFeeder = activateFeeder;
    }



}



