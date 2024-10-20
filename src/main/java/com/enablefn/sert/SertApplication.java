package com.enablefn.sert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.enablefn.sert.repository")
public class SertApplication {

	public static void main(String[] args) {
		SpringApplication.run(SertApplication.class, args);
	}

}
