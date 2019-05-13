package com.bancoazteca.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
public class ApiExtTransferenciasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiExtTransferenciasApplication.class, args);
	}

}
