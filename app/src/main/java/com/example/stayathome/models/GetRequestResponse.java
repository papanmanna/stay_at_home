package com.example.stayathome.models;

import com.example.stayathome.utils.Utils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetRequestResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private List<RequestData> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<RequestData> getData() {
        return data;
    }

    public void setData(List<RequestData> data) {
        this.data = data;
    }

    public class RequestData {
        @SerializedName("_id")
        private String id;
        @SerializedName("status")
        private String status;
        @SerializedName("station")
        private Station station;
        @SerializedName("reason")
        private String reason;
        @SerializedName("startHour")
        private int startHour;
        @SerializedName("endHour")
        private int endHour;
        @SerializedName("date")
        private long date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Station getStation() {
            return station;
        }

        public void setStation(Station station) {
            this.station = station;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public int getStartHour() {
            return startHour;
        }

        public void setStartHour(int startHour) {
            this.startHour = startHour;
        }

        public int getEndHour() {
            return endHour;
        }

        public void setEndHour(int endHour) {
            this.endHour = endHour;
        }

        public String getDate() {
            return Utils.getDateFromMillis(date);
        }

        public void setDate(long date) {
            this.date = date;
        }
    }

    public class User {
        @SerializedName("firstName")
        private String fName;
        @SerializedName("lastName")
        private String lName;

        public String getfName() {
            return fName;
        }

        public void setfName(String fName) {
            this.fName = fName;
        }

        public String getlName() {
            return lName;
        }

        public void setlName(String lName) {
            this.lName = lName;
        }
    }
}
