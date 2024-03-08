package com.enigma.wmbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WmbApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WmbApiApplication.class, args);
	}

}
