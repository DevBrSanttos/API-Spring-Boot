package com.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.cursomc.services.DBService;

@Configuration
@Profile("dev")
public class ProfileDevConfig {

	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategyGenerateBD;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		if(strategyGenerateBD.equals("create"))
			dbService.instantiateTestDatabase();
		return true;
	}
}
