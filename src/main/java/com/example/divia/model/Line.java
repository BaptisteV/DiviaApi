package com.example.divia.model;

import java.util.List;

// Domain models for API responses
public class Line {
    private String id;
    private String name;
    private String number;
    private String color;
    private String direction;
    private String type;
    private String terminus;
    private List<Stop> stops;
    private LigneData data;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public LigneData getData() {
        return data;
    }
}
