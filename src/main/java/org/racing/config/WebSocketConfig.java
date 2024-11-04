package org.racing.config;


import jakarta.annotation.PostConstruct;
import org.racing.services.RaceMaintainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer, WebMvcConfigurer {

    private final RaceMaintainer simulationService;

    // Inject uniquement le port depuis application.properties
    @Value("${interface.port}")
    private String interfacePort;

    // URL complète construite dans le code
    private String corsAllowedOrigin;

    public WebSocketConfig(RaceMaintainer simulationService) {
        this.simulationService = simulationService;
    }

    // Méthode appelée après l'injection des dépendances pour construire l'URL complète
    @PostConstruct
    public void init() {
        this.corsAllowedOrigin = "http://localhost:" + interfacePort;
        System.out.println("CORS allowed origin: " + corsAllowedOrigin);  // Affiche l'URL complète
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsAllowedOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new PositionHandler(simulationService), "/positions").setAllowedOrigins("*");
    }
}
