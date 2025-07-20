package com.example.divia.model.divia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReseauData {
    @JsonProperty("lignes")
    private Map<String, LigneData> lignes;

    @JsonProperty("arrets")
    private Map<String, Stop> arrets;

    public List<LigneData> getLignes() {
        return new ArrayList<>(lignes.values());
    }

    public List<Stop> getStops() {
        return new ArrayList<>(arrets.values());
    }
}
