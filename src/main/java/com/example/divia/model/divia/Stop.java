package com.example.divia.model.divia;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stop {
    @JsonProperty("id")
    private String id;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("id_ligne")
    private String idLigne;

    @JsonProperty("zone")
    private String zone;

    @JsonProperty("accessible")
    private boolean accessible;

    @JsonProperty("lat")
    private double lat;

    @JsonProperty("lng")
    private double lng;

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getIdLigne() {
        return idLigne;
    }

    public String getZone() {
        return zone;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
