package com.hakantahta.eticaret;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.hakantahta.eticaret.model")  // Entity'lerin bulunduğu paket
@EnableJpaRepositories("com.hakantahta.eticaret.repository") // Repository'lerin bulunduğu paket
public class EticaretApplication {

	public static void main(String[] args) {
		SpringApplication.run(EticaretApplication.class, args);
	}

}
