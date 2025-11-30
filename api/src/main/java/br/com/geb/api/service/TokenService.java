package br.com.geb.api.service;

import br.com.geb.api.domain.usuario.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {

    // ATENÇÃO: Para essa biblioteca, o segredo precisa ter pelo menos 32 caracteres (256 bits)
    @Value("${api.security.token.secret:meu-segredo-super-secreto-que-tem-pelo-menos-32-caracteres}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .setIssuer("GEB API")
                .setSubject(usuario.getEmail())
                .claim("id", usuario.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7200000)) // 2 horas (em milissegundos)
                .signWith(getChaveAssinatura(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getChaveAssinatura() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}