package com.example.stayathome.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Station {
    @SerializedName("pinCodes")
    private List<Integer> pinCodes;
    @SerializedName("_id")
    private String id;
    @SerializedName("stationName")
    private String stationName;

    public List<Integer> getPinCodes() {
        return pinCodes;
    }

    public void setPinCodes(List<Integer> pinCodes) {
        this.pinCodes = pinCodes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
