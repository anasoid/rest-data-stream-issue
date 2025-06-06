package com.example.rest.data.stream.issue;

import org.springframework.boot.SpringApplication;

public class TestRestDataStreamIssueApplication {

	public static void main(String[] args) {
		SpringApplication.from(RestDataStreamIssueApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
