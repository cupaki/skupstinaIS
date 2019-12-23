package com.skupstina.arhiv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@SpringBootApplication
@ComponentScan
public class App 
{
	
	@Configuration
	public static class RestConfiguration {

	    @Bean
	    public CorsFilter corsFilter() {

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        CorsConfiguration config = new CorsConfiguration();
	        config.addAllowedOrigin("*");
	        config.addAllowedHeader("*");
	        config.addAllowedMethod("GET");
	        config.addAllowedMethod("PUT");
	        config.addAllowedMethod("POST");
	        source.registerCorsConfiguration("/**", config);
	        return new CorsFilter(source);
	    }
	}
	
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
}
