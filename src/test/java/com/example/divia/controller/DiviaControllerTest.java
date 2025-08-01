package com.example.divia.controller;

import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class DiviaControllerTest {
    private static final int PORT = 6379;
    //@Container
    //private static final GenericContainer container = new GenericContainer(DockerImageName.parse("eclipse-temurin:21-alpine")).withExposedPorts(PORT);

    @Container
    private static final GenericContainer appContainer = new GenericContainer(new ImageFromDockerfile());
    private final WebClient webClient;

    public DiviaControllerTest(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://" + appContainer.getHost() + ":" + "8080" + "/api/v1").build();
    }
    
    @BeforeAll
    static void beforeAll() {
        appContainer.start();
        //container.start();
    }

    @AfterAll
    static void afterAll() {
        appContainer.stop();
        //container.stop();
    }
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getMorning() {
        String res = webClient.get().uri("/morning").retrieve().bodyToMono(String.class).block();
    }
}