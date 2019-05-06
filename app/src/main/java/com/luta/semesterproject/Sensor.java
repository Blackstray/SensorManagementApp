package com.luta.semesterproject;

import android.renderscript.Sampler;

import com.google.firebase.database.collection.ArraySortedMap;
import com.google.firestore.v1.Value;

import java.io.Serializable;
import java.security.Key;

public class Sensor implements  Serializable{

    private double voltage, amperage, power;
    private String type, name, status;

    public Sensor() {}

    public Sensor(ArraySortedMap<String,String> array){
        this.amperage = Double.parseDouble(array.get("amperage"));
        this.power = Double.parseDouble(array.get("power"));
        this.voltage = Double.parseDouble(array.get("voltage"));
        this.status = array.get("status");
        this.type = array.get("type");
        this.name = array.get("name");
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
