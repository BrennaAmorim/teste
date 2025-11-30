package br.com.geb.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/publico")
public class StatusController {

    @GetMapping("/status")
    public String verificarStatus() {
        return "Backend GEB rodando com sucesso! Hora do servidor: " + java.time.LocalDateTime.now();
    }
}