package com.ge.predix.alm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableAutoConfiguration
@EnableResourceServer
@ComponentScan
public class AlmApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlmApplication.class, args);
	}
}
