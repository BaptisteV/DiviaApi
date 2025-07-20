package com.example.divia.model;

import com.example.divia.model.divia.HoraireResponse;
import com.example.divia.model.divia.TotemResponse;
import com.example.divia.model.openmeteo.MeteoResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MorningResponse {
    @JsonProperty("horairesFoch")
    private List<HoraireResponse> horairesFoch;

    @JsonProperty("meteo")
    private MeteoResponse meteo;

    public MorningResponse(TotemResponse totemResponse, MeteoResponse meteoApiResponse) {
        this.horairesFoch = totemResponse.getHoraires();
        this.meteo = meteoApiResponse;
    }
}
