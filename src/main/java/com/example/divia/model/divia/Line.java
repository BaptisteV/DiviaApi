package com.example.divia.model.divia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Line {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("number")
    private String number;
    @JsonProperty("color")
    private String color;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("type")
    private String type;
    @JsonProperty("terminus")
    private String terminus;
    @JsonProperty("stops")
    private List<Stop> stops;
    @JsonProperty("data")
    private final LigneData data;

    public Line(LigneData data) {
        this.data = data;
        this.id = data.getId();
        this.name = data.getNom();
        this.number = data.getNum();
        this.color = data.getCouleur();
        this.direction = data.getSens();
        this.type = data.getType();
        this.terminus = data.getTerminus();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getDirection() {
        return direction;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }
}
