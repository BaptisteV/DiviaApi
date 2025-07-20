package com.example.divia.model.divia;

public class Stop {
    private final String id;
    private final String name;
    private final String lineId;
    private final String zone;
    private final boolean accessible;
    private final double latitude;
    private final double longitude;
    private final ArretData data;

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

    public String getName() {
        return name;
    }
}
