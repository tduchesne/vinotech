package com.vinotech.sommelier_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Lit la propriété depuis application.properties
    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Applique les règles à toutes les routes API
                .allowedOrigins(allowedOrigins.split(",")) // Supporte plusieurs origines séparées par des virgules
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false); // Mettre à true si besoin de cookies/auth, mais nécessite des origines explicites (pas *)
    }
}