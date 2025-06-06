package com.example.rest.data.stream.issue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("com.example.rest.data.stream.issue.repository")
@EntityScan("com.example.rest.data.stream.issue.domain")
public class RestDataStreamIssueApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestDataStreamIssueApplication.class, args);
	}

}
