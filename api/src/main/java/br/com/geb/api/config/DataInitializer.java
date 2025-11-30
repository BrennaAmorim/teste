package br.com.geb.api.config;

import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.enums.Papel;
import br.com.geb.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe um admin para não criar duplicado toda vez que reiniciar
        if (usuarioRepository.findByEmail("admin@geb.com.br").isEmpty()) {
            
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setUsername("admin");
            admin.setEmail("admin@geb.com.br");
            
            // A mágica acontece aqui: transformamos "123456" em hash BCrypt
            admin.setSenha(passwordEncoder.encode("123456")); 
            
            admin.setPapel(Papel.ROLE_ADMIN);

            usuarioRepository.save(admin);
            
            System.out.println("---------------------------------");
            System.out.println("USUÁRIO ADMIN CRIADO COM SUCESSO!");
            System.out.println("Login: admin@geb.com.br");
            System.out.println("Senha: 123456");
            System.out.println("---------------------------------");
        }
    }
}