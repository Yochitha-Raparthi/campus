//package com.campuspulse.config;
//
//import com.campuspulse.security.JwtFilter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.*;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.*;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.*;
//import java.util.*;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Configuration
//@EnableMethodSecurity
//public class SecurityConfig {
//	@Bean
//	PasswordEncoder encoder() {
//		return new BCryptPasswordEncoder();
//	}
//
////	@Bean
////	CorsConfigurationSource cors(@Value("${app.cors.origins:http://localhost:5173}") String origins) {
////		var c = new CorsConfiguration();
////		c.setAllowedOrigins(Arrays.stream(origins.split(",")).map(String::trim).toList());
////		c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
////		c.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
////		c.setAllowCredentials(false);
////		c.setMaxAge(3600L);
////		var s = new UrlBasedCorsConfigurationSource();
////		s.registerCorsConfiguration("/**", c);
////		return s;
////	}
//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//
//	    CorsConfiguration config = new CorsConfiguration();
//
//	    config.setAllowedOrigins(
//	        List.of("http://localhost:5173")
//	    );
//
//	    config.setAllowedMethods(
//	        List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
//	    );
//
//	    config.setAllowedHeaders(
//	        List.of("Authorization", "Content-Type")
//	    );
//
//	    config.setExposedHeaders(
//	        List.of("Authorization")
//	    );
//
//	    config.setAllowCredentials(false);
//
//	    UrlBasedCorsConfigurationSource source =
//	        new UrlBasedCorsConfigurationSource();
//
//	    source.registerCorsConfiguration("/**", config);
//
//	    return source;
//	}
//
//	@Bean
//	SecurityFilterChain filter(HttpSecurity http, JwtFilter jwt) throws Exception {
//		return http.csrf(c -> c.disable()).cors(c -> {
//		}).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.authorizeHttpRequests(a -> a.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//						.requestMatchers("/api/auth/login", "/api/auth/signup", "/actuator/health").permitAll()
//						.requestMatchers("/api/admin/**").hasRole("ADMIN").requestMatchers("/api/staff/**")
//						.hasRole("STAFF").requestMatchers("/api/**").authenticated().anyRequest().denyAll())
//				.exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> {
//					res.setStatus(401);
//					res.setContentType("application/json");
//					res.getWriter().write("{\"message\":\"Authentication required\"}");
//				}).accessDeniedHandler((req, res, ex) -> {
//					res.setStatus(403);
//					res.setContentType("application/json");
//					res.getWriter().write("{\"message\":\"Access denied\"}");
//				})).addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class).build();
//	}
//}


package com.campuspulse.config;

import com.campuspulse.security.JwtFilter;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.origins:http://localhost:5173}") String origins) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.stream(origins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filter(HttpSecurity http, JwtFilter jwt,
                                      CorsConfigurationSource corsConfigurationSource) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/login", "/api/auth/signup", "/actuator/health").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/staff/**").hasRole("STAFF")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().denyAll())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, error) -> {
                            res.setStatus(401);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"message\":\"Authentication required\"}");
                        })
                        .accessDeniedHandler((req, res, error) -> {
                            res.setStatus(403);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"message\":\"Access denied\"}");
                        }))
                .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
