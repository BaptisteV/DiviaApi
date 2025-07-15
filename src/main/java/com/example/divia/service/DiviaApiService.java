package com.example.divia.service;

import com.example.divia.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.stream.Collectors;

@Service
public class DiviaApiService {
    private static final int TotemResponseCacheDurationInMinute = 5;
    private static final Logger logger = LoggerFactory.getLogger(DiviaApiService.class);

    private static final String RESEAU_API_URL = "https://bo-api.divia.fr/api/reseau/type/json";
    private static final String TOTEM_API_URL = "https://www.divia.fr/bus-tram";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private ReseauData reseauData;
    private Map<String, Line> linesById;
    private Map<String, Stop> stopsById;
    private Map<String, List<Stop>> stopsByLineId;

    public DiviaApiService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
        this.linesById = new HashMap<>();
        this.stopsById = new HashMap<>();
        this.stopsByLineId = new HashMap<>();
    }

    /**
     * Initialize the API by loading network data from Divia
     */
    public void init() {
        try {
            logger.info("Initializing Divia API...");

            String json = webClient
                    .mutate()
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024))
                    .build()
                    .get()
                    .uri(RESEAU_API_URL)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            reseauData = objectMapper.readValue(json, ReseauData.class);

            // Build lookup maps
            buildLookupMaps();

            logger.info("Divia API initialized successfully. Lines: {}, Stops: {}",
                    linesById.size(), stopsById.size());

        } catch (Exception e) {
            logger.error("Failed to initialize Divia API", e);
            throw new RuntimeException("Failed to initialize Divia API", e);
        }
    }

    private void buildLookupMaps() {
        // Build lines map
        for (LigneData ligneData : reseauData.getLignes()) {
            Line line = new Line(ligneData);
            linesById.put(ligneData.getId(), line);
        }

        // Build stops map and associate with lines
        for (ArretData arretData : reseauData.getArrets()) {
            Stop stop = new Stop(arretData);
            stopsById.put(arretData.getId(), stop);

            // Group stops by line
            stopsByLineId.computeIfAbsent(arretData.getIdLigne(), k -> new ArrayList<>()).add(stop);
        }

        // Set stops for each line
        for (Line line : linesById.values()) {
            List<Stop> lineStops = stopsByLineId.get(line.getId());
            if (lineStops != null) {
                line.setStops(lineStops);
            }
        }
    }

    /**
     * Get all lines
     */
    public List<Line> getLines() {
        ensureInitialized();
        return new ArrayList<>(linesById.values());
    }

    /**
     * Find a line by number and direction
     */
    public Optional<Line> findLine(String lineNumber, String direction) {
        ensureInitialized();

        // Default direction is 'A' if not specified
        String searchDirection = (direction != null && !direction.isEmpty()) ? direction : "A";

        return linesById.values().stream()
                .filter(line -> lineNumber.equals(line.getNumber()) &&
                        searchDirection.equals(line.getDirection()))
                .findFirst();
    }

    /**
     * Get a line by its ID
     */
    public Optional<Line> getLine(String lineId) {
        ensureInitialized();
        return Optional.ofNullable(linesById.get(lineId));
    }

    /**
     * Find a stop by line number, stop name, and direction
     */
    public Optional<Stop> findStop(String lineNumber, String stopName, String direction) {
        Optional<Line> line = findLine(lineNumber, direction);
        if (line.isEmpty()) {
            return Optional.empty();
        }

        return line.get().getStops().stream()
                .filter(stop -> stopName.equals(stop.getName()))
                .findFirst();
    }

    /**
     * Get a stop by its ID
     */
    public Optional<Stop> getStop(String stopId) {
        ensureInitialized();
        return Optional.ofNullable(stopsById.get(stopId));
    }

    /**
     * Find stops by line ID
     */
    public List<Stop> getStopsByLineId(String lineId) {
        ensureInitialized();
        return stopsByLineId.getOrDefault(lineId, new ArrayList<>());
    }

    private LocalDateTime lastFetch = LocalDateTime.MIN;

    private boolean horairesExpired() {
        return LocalDateTime.now().minusMinutes(TotemResponseCacheDurationInMinute).isAfter(lastFetch);
    }

    private TotemResponse lastResponse;

    /**
     * Get next passages for a stop using the TOTEM service
     */
    public TotemResponse getTotem(String stopId, String lineId) {
        ensureInitialized();

        if (!horairesExpired()) {
            LocalDateTime now = LocalDateTime.now();
            List<HoraireResponse> updatedHoraires = new ArrayList<>();
            for (LocalDateTime t : lastResponse.getHoraires().stream().map(s -> s.getArrivesAt()).toList()) {
                updatedHoraires.add(new HoraireResponse(now, t));
            }
            lastResponse.setHoraires(updatedHoraires);
            return lastResponse;
        }

        Optional<Stop> stop = getStop(stopId);
        Optional<Line> line = getLine(lineId);

        if (stop.isEmpty() || line.isEmpty()) {
            throw new IllegalArgumentException("Stop or line not found");
        }

        try {
            // Prepare form data
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("requete", "arret_prochainpassage");
            formData.add("requete_val[id_ligne]", lineId);
            formData.add("requete_val[id_arret]", stopId);

            // Make POST request to TOTEM API
            String response = webClient.post()
                    .uri(TOTEM_API_URL + "?type=479")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse the HTML response
            TotemResponse totemResponse = new TotemResponse(
                    stopId,
                    stop.get().getName(),
                    lineId,
                    line.get().getName()
            );

            LocalDateTime now = LocalDateTime.now();
            totemResponse.setRawHtml(response);
            totemResponse.setHoraires(parseHoraireResponse(response, now));

            lastFetch = now;
            lastResponse = totemResponse;
            return totemResponse;

        } catch (Exception e) {
            logger.error("Failed to get TOTEM data for stop {} on line {}", stopId, lineId, e);
            throw new RuntimeException("Failed to get TOTEM data", e);
        }
    }

    private List<HoraireResponse> parseHoraireResponse(String html, LocalDateTime receivedAt) {
        List<HoraireResponse> horaires = new ArrayList<>();

        try {
            Document doc = Jsoup.parse(html);

            Elements timeElements = doc.select(".uk-badge");

            for (Element element : timeElements) {
                String timeText = element.text().trim();

                LocalDateTime passage = parseTimeString(timeText);
                HoraireResponse horaire = new HoraireResponse(receivedAt, passage);

                if (passage != null) {
                    horaires.add(horaire);
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to parse TOTEM response", e);
        }

        return horaires.stream()
                .collect(Collectors.toList());
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

    private void ensureInitialized() {
        if (reseauData == null) {
            init();
        }
    }

    /**
     * Get the raw network data
     */
    public ReseauData getReseauData() {
        ensureInitialized();
        return reseauData;
    }
}