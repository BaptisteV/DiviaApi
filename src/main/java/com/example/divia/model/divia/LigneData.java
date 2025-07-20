package com.example.divia.model.divia;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LigneData {
    @JsonProperty("id")
    private String id;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("num")
    private String num;

    @JsonProperty("couleur")
    private String couleur;

    @JsonProperty("sens")
    private String sens;

    @JsonProperty("type")
    private String type;

    @JsonProperty("terminus")
    private String terminus;

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getNum() {
        return num;
    }

    public String getCouleur() {
        return couleur;
    }

    public String getSens() {
        return sens;
    }

    public String getType() {
        return type;
    }

    public String getTerminus() {
        return terminus;
    }
}

