package com.grupo6.barbearia_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BarbeariaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarbeariaApiApplication.class, args);
	}

}
