package com.example.divia.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Network data structure from Divia API
public class ReseauData {
    @JsonProperty("lignes")
    private Map<String, LigneData> lignes;

    @JsonProperty("arrets")
    private Map<String, ArretData> arrets;

    // Getters and setters
    public List<LigneData> getLignes() {
        return new ArrayList<>(lignes.values());
    }

    public void setLignes(Map<String, LigneData> lignes) {
        this.lignes = lignes;
    }

    public List<ArretData> getArrets() {
        return new ArrayList<>(arrets.values());
    }

    public void setArrets(Map<String, ArretData> arrets) {
        this.arrets = arrets;
    }

}
