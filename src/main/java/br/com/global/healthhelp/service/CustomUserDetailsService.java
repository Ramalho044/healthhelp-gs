package br.com.global.healthhelp.service;

import br.com.global.healthhelp.model.Usuario;
import br.com.global.healthhelp.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;

    public CustomUserDetailsService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        if ("admin".equalsIgnoreCase(login)) {
            return User.builder()
                    .username("admin")
                    .password("$2b$10$g5cqvgjYJizQp/CCH909hur9aWkBE.gVqyWtQeoQRG85VRx/0lLKW")
                    .roles("ADMIN")
                    .build();
        }

        Usuario usuario = usuarioRepo.findByEmail(login)
                .orElseGet(() -> usuarioRepo.findByNome(login)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("Usuário/E-mail não encontrado: " + login)
                        ));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles("USER")
                .build();
    }
}
