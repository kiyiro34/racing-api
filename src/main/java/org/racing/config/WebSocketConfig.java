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

    @Value("${interface.port}")
    private String interfacePort;

    private String corsAllowedOrigin;

    public WebSocketConfig(RaceMaintainer simulationService) {
        this.simulationService = simulationService;
    }

    @PostConstruct
    public void init() {
        this.corsAllowedOrigin = "http://localhost:" + interfacePort;
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
