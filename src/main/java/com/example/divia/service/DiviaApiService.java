package com.example.divia.service;

import com.example.divia.SimpleCache;
import com.example.divia.model.divia.*;
import jakarta.annotation.PostConstruct;
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
import java.util.*;

@Service
public class DiviaApiService {
    private final static String stopIdFoch = "1467";
    private final static String lineIdFochToValmy = "96";
    private static final int TotemResponseCacheDurationInSeconds = 60;
    private final SimpleCache<TotemResponse> cache;

    private final WebClient webClient;
    private static final String RESEAU_API_URL = "https://bo-api.divia.fr/api/reseau/type/json";
    private static final String TOTEM_API_URL = "https://www.divia.fr/bus-tram";

    private final Map<String, Line> linesById;
    private final Map<String, Stop> stopsById;
    private final Map<String, List<Stop>> stopsByLineId;

    private static final Logger logger = LoggerFactory.getLogger(DiviaApiService.class);

    public DiviaApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(8 * 1024 * 1024)).build();
        this.linesById = new HashMap<>();
        this.stopsById = new HashMap<>();
        this.stopsByLineId = new HashMap<>();
        this.cache = new SimpleCache<>(this::getTotemFromApi, TotemResponseCacheDurationInSeconds);
    }

    @PostConstruct
    private void init() {
        try {
            logger.info("Initializing Divia API...");

            ReseauData reseauData = webClient
                    .get()
                    .uri(RESEAU_API_URL)
                    .retrieve()
                    .bodyToMono(ReseauData.class)
                    .block();

            buildLookupMaps(reseauData);

            cache.Set(getTotemFromApi());
            logger.info("Divia API initialized successfully. Lines: {}, Stops: {}", linesById.size(), stopsById.size());
        } catch (Exception e) {
            logger.error("Failed to initialize Divia API", e);
            throw new RuntimeException("Failed to initialize Divia API", e);
        }
    }

    private void buildLookupMaps(ReseauData reseauData) {
        // Build lines map
        for (LigneData ligneData : reseauData.getLignes()) {
            Line line = new Line(ligneData);
            linesById.put(ligneData.getId(), line);
        }

        // Build stops map and associate with lines
        for (Stop stop : reseauData.getStops()) {
            stopsById.put(stop.getId(), stop);

            // Group stops by line
            stopsByLineId.computeIfAbsent(stop.getIdLigne(), k -> new ArrayList<>()).add(stop);
        }

        // Set stops for each line
        for (Line line : linesById.values()) {
            List<Stop> lineStops = stopsByLineId.get(line.getId());
            if (lineStops != null) {
                line.setStops(lineStops);
            }
        }
    }

    public List<Line> getLines() {
        return new ArrayList<>(linesById.values());
    }

    public Optional<Line> findLine(String lineNumber, String direction) {
        // Default direction is 'A' if not specified
        String searchDirection = (direction != null && !direction.isEmpty()) ? direction : "A";

        return linesById.values().stream()
                .filter(line -> lineNumber.equals(line.getNumber()) &&
                        searchDirection.equals(line.getDirection()))
                .findFirst();
    }

    public Optional<Line> getLine(String lineId) {
        return Optional.ofNullable(linesById.get(lineId));
    }

    public Optional<Stop> findStop(String lineNumber, String stopName, String direction) {
        Optional<Line> line = findLine(lineNumber, direction);
        return line.flatMap(value -> value.getStops().stream()
                .filter(stop -> stopName.equals(stop.getNom()))
                .findFirst());
    }

    public Optional<Stop> getStop(String stopId) {
        return Optional.ofNullable(stopsById.get(stopId));
    }

    public List<Stop> getStopsByLineId(String lineId) {
        return stopsByLineId.getOrDefault(lineId, new ArrayList<>());
    }

    public TotemResponse getTotem() {
        TotemResponse response = cache.Get();
        response.refreshMinutesLeft();
        return response;
    }

    public TotemResponse getTotemFromApi() {
        Optional<Stop> stop = getStop(stopIdFoch);
        Optional<Line> line = getLine(lineIdFochToValmy);

        if (stop.isEmpty() || line.isEmpty()) {
            throw new IllegalArgumentException("Stop or line not found");
        }

        // Prepare form data
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("requete", "arret_prochainpassage");
        formData.add("requete_val[id_ligne]", lineIdFochToValmy);
        formData.add("requete_val[id_arret]", stopIdFoch);

        return webClient.post()
                .uri(TOTEM_API_URL + "?type=479")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("X-Requested-With", "XMLHttpRequest")
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    TotemResponse totemResponse = new TotemResponse(
                            stopIdFoch,
                            stop.get().getNom(),
                            lineIdFochToValmy,
                            line.get().getName()
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