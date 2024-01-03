package com.umc.StudyFlexBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyFlexBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyFlexBeApplication.class, args);
	}

}
