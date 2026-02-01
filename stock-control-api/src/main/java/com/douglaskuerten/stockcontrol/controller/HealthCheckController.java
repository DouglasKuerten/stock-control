package com.douglaskuerten.stockcontrol.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check")
public class HealthCheckController {
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
