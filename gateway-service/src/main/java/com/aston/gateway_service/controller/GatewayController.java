package com.aston.gateway_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Slf4j
public class GatewayController {

    @Autowired
    private RouteLocator routeLocator;

    @GetMapping(value = "/routes")
    public Mono<Map<String, Object>> getRoutes() {
        return routeLocator.getRoutes().collectList()
            .map(routes -> Map.of("routes",
                routes.stream()
                    .map(route -> Map.of(
                        "id", route.getId(),
                        "uri", route.getUri().toString(),
                        "predicate", route.getPredicate().toString(),
                        "filters", route.getFilters().toString()
                    )).collect(Collectors.toList()), "count", routes.size()))
            .doOnError(e -> log.error("Error getting routes", e))
            .onErrorResume(e -> Mono.just(Map.of(
                "error", e.getMessage(),
                "count", 0
            )));
    }
}
