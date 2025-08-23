package com.example.RedisPractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RedisPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisPracticeApplication.class, args);
	}

}
