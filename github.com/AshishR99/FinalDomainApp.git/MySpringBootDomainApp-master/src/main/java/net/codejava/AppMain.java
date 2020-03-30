package net.codejava;


//FILE PATH DIRECTORY NAME : E:\GIT DomainApp

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.codejava.entities.Userlogin;
import net.codejava.payload.SignUpPayload;
import net.codejava.services.UserloginService;


@SpringBootApplication
public class AppMain<CorsConfiguration> {

	
	public static void main(String[] args) {
		SpringApplication.run(AppMain.class, args);
	}
	
	@Configuration
	@EnableWebMvc
	public class WebConfig implements WebMvcConfigurer {
	 
	    @Override
	    public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**");
	    }
	}
	
	
	@Bean(name="corsConfigurationSource")
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = (CorsConfiguration) new org.springframework.web.cors.CorsConfiguration();
        ((org.springframework.web.cors.CorsConfiguration) configuration).setAllowedOrigins(Arrays.asList("*"));
        ((org.springframework.web.cors.CorsConfiguration) configuration).setAllowedMethods(Arrays.asList( "OPTIONS","GET", "POST", "DELETE", "PUT","PATCH"));
        ((org.springframework.web.cors.CorsConfiguration) configuration).setAllowedHeaders(Arrays.asList("Content-Type", "content-type", "x-requested-with", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "x-auth-token", "x-app-id", "Origin","Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", (org.springframework.web.cors.CorsConfiguration) configuration);
        return source;
    }
	
	
	  @Bean PasswordEncoder getEncoder() { return new BCryptPasswordEncoder(); }
	 

	
	@Bean
    public CommandLineRunner demoData(UserloginService userloginService) {
        return args -> 
        { 
        	userloginService.save(new SignUpPayload(1,"admin@gmail.com","admin","admin","8785476866",1,"NA","",1));
        };
    }
	
	
}


