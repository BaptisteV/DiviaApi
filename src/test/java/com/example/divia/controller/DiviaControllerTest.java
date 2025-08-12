package com.example.divia.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
@SpringBootTest
public class DiviaControllerTest {
    //private static final int PORT = 8080;

    //. withDockerfile(Path.of("../../../Dockerfile"))).withExposedPorts(PORT)
    @Container
    private static final GenericContainer appContainer = new GenericContainer(new ImageFromDockerfile().withDockerfile(Paths.get(System.getProperty("user.dir") + "/Dockerfile")));
    private final WebClient.Builder webClientBuilder;

    @Autowired
    DiviaControllerTest(WebClient.Builder webClientBuilder){
        this.webClientBuilder = webClientBuilder;
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
        var client = webClientBuilder.baseUrl("http://" + appContainer.getHost() + "/api/v1").build();
        String res = client.get().uri("/morning").retrieve().bodyToMono(String.class).block();
    }
}