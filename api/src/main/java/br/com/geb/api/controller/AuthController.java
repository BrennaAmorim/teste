package br.com.geb.api.controller;

import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.dto.LoginRequest;
import br.com.geb.api.dto.TokenDTO;
import br.com.geb.api.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth") // <--- CORREÇÃO: Removemos o "/api" para bater com o site
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginRequest dados) {
        
        // 1. O Spring Security checa se a senha "123456" bate com o hash do banco
        var usernamePassword = new UsernamePasswordAuthenticationToken(dados.getEmail(), dados.getSenha());
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        
        // 2. Se a senha estiver certa, geramos o token
        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());
        
        return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
    }
}