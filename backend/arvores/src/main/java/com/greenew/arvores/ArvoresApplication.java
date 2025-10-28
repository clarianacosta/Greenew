package com.greenew.arvores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ArvoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArvoresApplication.class, args);
	}

}
