package com.magna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableScheduling
public class MagnaInternalTrackerApplication extends SpringBootServletInitializer {
//
//	 @Override
//	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//	        return application.sources(MagnaInternalTrackerApplication.class);
//	    }

	 
	public static void main(String[] args) {
		SpringApplication.run(MagnaInternalTrackerApplication.class, args);
	}
	
}
