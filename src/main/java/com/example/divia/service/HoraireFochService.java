package com.example.divia.service;

import com.example.divia.model.TotemResponse;
import org.springframework.stereotype.Service;

@Service
public class HoraireFochService {
    private final DiviaApiService diviaApiService;

    public HoraireFochService(DiviaApiService diviaApiService){
        this.diviaApiService = diviaApiService;
    }

    public TotemResponse getFoch()
    {
        String stopId = "1467";
        String lineId = "96";
        return diviaApiService.getTotem(stopId, lineId);
    }
}
