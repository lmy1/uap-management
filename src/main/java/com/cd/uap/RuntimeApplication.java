package com.cd.uap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * 引导类
 * @author huntto
 *
 */
@SpringBootApplication
@EnableWebSecurity
@ImportResource(locations={"classpath:spring-security.xml"})
public class RuntimeApplication {
	public static void main(String[] args) {
		SpringApplication.run(RuntimeApplication.class, args);
	}
}
