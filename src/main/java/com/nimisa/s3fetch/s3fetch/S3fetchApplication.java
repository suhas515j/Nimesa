package com.nimisa.s3fetch.s3fetch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class S3fetchApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3fetchApplication.class, args);
	}

}
