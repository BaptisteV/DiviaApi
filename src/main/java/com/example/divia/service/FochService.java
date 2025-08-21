package com.example.divia.service;

import com.example.divia.SimpleCache;
import com.example.divia.model.FochResponse;
import com.example.divia.model.divia.HoraireResponse;
import com.example.divia.model.divia.TotemResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FochService {
    private final String lineId = "96";
    private final String lineName = " > DIJON Valmy";
    private final String stopId = "1467";
    private final String stopName = "Foch Gare";
    private static final int TotemResponseCacheDurationInSeconds = 60;
    private final SimpleCache<TotemResponse> cache;

    private final WebClient webClient;
    private static final String TOTEM_API_URL = "https://www.divia.fr/bus-tram";

    private static final Logger logger = LoggerFactory.getLogger(FochService.class);

    public FochService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(8 * 1024 * 1024)).build();
        this.cache = new SimpleCache<>(this::getTotemFromApi, TotemResponseCacheDurationInSeconds);
    }

    public FochResponse getFoch() {
        TotemResponse response = cache.Get();
        response.refreshMinutesLeft();
        return new FochResponse(response);
    }

    private TotemResponse getTotemFromApi() {
        // Prepare form data
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("requete", "arret_prochainpassage");
        formData.add("requete_val[id_ligne]", lineId);
        formData.add("requete_val[id_arret]", stopId);

        return webClient.post()
                .uri(TOTEM_API_URL + "?type=479")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("X-Requested-With", "XMLHttpRequest")
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    TotemResponse totemResponse = new TotemResponse(
                            stopId,
                            stopName,
                            lineId,
                            lineName
                    );

                    LocalDateTime now = LocalDateTime.now();
                    totemResponse.setHoraires(parseHoraireResponse(response, now));
                    return totemResponse;
                }).block();
    }

    private List<HoraireResponse> parseHoraireResponse(String html, LocalDateTime receivedAt) {
        List<HoraireResponse> horaires = new ArrayList<>();

        try {
            Document doc = Jsoup.parse(html);

            Elements timeElements = doc.select(".uk-badge");

            for (Element element : timeElements) {
                String timeText = element.text().trim();

                LocalDateTime passage = parseTimeString(timeText);
                HoraireResponse horaire = new HoraireResponse(passage, receivedAt);

                if (passage != null) {
                    horaires.add(horaire);
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to parse TOTEM response", e);
        }

        return new ArrayList<>(horaires);
    }

    private LocalDateTime parseTimeString(String timeText) {
        try {
            // Handle "HH:MM" format
            if (timeText.matches("\\d{1,2}:\\d{2}")) {
                return LocalDateTime.now().with(
                        LocalDateTime.parse(LocalDateTime.now().toLocalDate() + "T" + timeText + ":00")
                                .toLocalTime()
                );
            }
        } catch (Exception e) {
            logger.debug("Could not parse time string: {}", timeText);
        }

        return null;
    }
}