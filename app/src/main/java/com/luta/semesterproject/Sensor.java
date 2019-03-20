package com.luta.semesterproject;

import java.io.Serializable;

public class Sensor implements  Serializable{

    private double voltage, amperage, power;
    private String type, name, status;

    public Sensor() {}

    public Sensor(double amperage, double power, double voltage, String type, String name, String status) {
        this.amperage = amperage;
        this.power = power;
        this.voltage = voltage;
        this.type = type;
        this.name = name;
        this.status = status;
    }

    public double getAmperage() {
        return amperage;
    }

    public void setAmperage(double amperage) {
        this.amperage = amperage;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
