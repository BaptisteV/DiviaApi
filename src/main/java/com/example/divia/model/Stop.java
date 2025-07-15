package com.example.divia.model;

public class Stop {
    private String id;
    private String name;
    private String lineId;
    private String zone;
    private boolean accessible;
    private double latitude;
    private double longitude;
    private ArretData data;

    public Stop(ArretData data) {
        this.data = data;
        this.id = data.getId();
        this.name = data.getNom();
        this.lineId = data.getIdLigne();
        this.zone = data.getZone();
        this.accessible = data.isAccessible();
        this.latitude = data.getLat();
        this.longitude = data.getLng();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ArretData getData() {
        return data;
    }
}
