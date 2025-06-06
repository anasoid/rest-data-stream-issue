package com.example.rest.data.stream.issue.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class PersonControllerTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Container
  static MongoDBContainer mongoDbContainer =
      new MongoDBContainer(DockerImageName.parse("mongo:latest"));

  @DynamicPropertySource
  static void dynamicPropertySource(DynamicPropertyRegistry registry) {
    mongoDbContainer.start();
    registry.add("spring.data.mongodb.uri", mongoDbContainer::getConnectionString);
    registry.add("spring.data.mongodb.database", () -> "test-data");
  }

  @Test
  void getListPerson() throws Exception {
    assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/persons", String.class))
        .contains("totalElements");
  }
}
