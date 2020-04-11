package com.example.stayathome.models;

import com.google.gson.annotations.SerializedName;

public class CreateRequestResponse {

    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private Data data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("request")
        private RequestData request;

        public RequestData getRequest() {
            return request;
        }

        public void setRequest(RequestData request) {
            this.request = request;
        }
    }
    public class RequestData {
        @SerializedName("_id")
        private String id;
        @SerializedName("status")
        private String status;
        @SerializedName("station")
        private String station;
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

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
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


        public void setDate(long date) {
            this.date = date;
        }
    }
}
