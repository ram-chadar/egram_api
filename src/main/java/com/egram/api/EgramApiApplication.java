package com.egram.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EgramApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EgramApiApplication.class, args);
		System.err.println("App started..");
	}
}
