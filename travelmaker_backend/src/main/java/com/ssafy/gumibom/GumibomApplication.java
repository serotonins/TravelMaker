package com.ssafy.gumibom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class GumibomApplication {

	public static void main(String[] args) {
		log.info("Gumibom Spring 프로젝트 접속!!");
		SpringApplication.run(GumibomApplication.class, args);
	}

}
