package com.example.divia.model;

import com.example.divia.model.divia.HoraireResponse;
import com.example.divia.model.divia.TotemResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FochResponse {
    @JsonProperty("horairesFoch")
    private List<HoraireResponse> horairesFoch;

    public FochResponse(TotemResponse totemResponse)
    {
        this.horairesFoch = totemResponse.getHoraires();
    }
}
