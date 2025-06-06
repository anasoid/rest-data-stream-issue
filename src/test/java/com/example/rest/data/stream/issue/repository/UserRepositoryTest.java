package com.example.rest.data.stream.issue.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.rest.data.stream.issue.domain.Person;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest()
@Testcontainers
class UserRepositoryTest {
  @Autowired private PersonRepository personRepository;

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
  void test() {
    Person person = new Person();
    person.setName("my name");
    Person savedPerson = personRepository.save(person);

    savedPerson.setName("updatedName");
    personRepository.save(savedPerson);

    Optional<Person> updatedPerson = personRepository.findById(person.getId());
    assertThat(updatedPerson)
        .isPresent()
        .get()
        .extracting(Person::getName)
        .isEqualTo("updatedName");
  }
}
