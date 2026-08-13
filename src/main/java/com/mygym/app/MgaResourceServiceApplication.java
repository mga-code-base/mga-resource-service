package com.mygym.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;

@SpringBootApplication(exclude = {
	    MongoAutoConfiguration.class
	})
public class MgaResourceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MgaResourceServiceApplication.class, args);
	}

}
