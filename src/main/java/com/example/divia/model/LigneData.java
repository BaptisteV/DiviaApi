package com.example.divia.model;

import com.fasterxml.jackson.annotation.JsonProperty;

// Line data from Divia API
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

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getSens() {
        return sens;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTerminus() {
        return terminus;
    }

    public void setTerminus(String terminus) {
        this.terminus = terminus;
    }
}

