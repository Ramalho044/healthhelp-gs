package br.com.global.healthhelp.controller;

import br.com.global.healthhelp.dto.RegistroDiarioDTO;
import br.com.global.healthhelp.model.Usuario;
import br.com.global.healthhelp.repository.UsuarioRepository;
import br.com.global.healthhelp.service.RegistroDiarioService;
import br.com.global.healthhelp.service.RecomendacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registros")
public class RegistroDiarioController {

    private final RegistroDiarioService registroService;
    private final RecomendacaoService recomendacaoService;
    private final UsuarioRepository usuarioRepository;

    public RegistroDiarioController(RegistroDiarioService registroService,
                                    RecomendacaoService recomendacaoService,
                                    UsuarioRepository usuarioRepository) {
        this.registroService = registroService;
        this.recomendacaoService = recomendacaoService;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
    }

    @PostMapping
    public ResponseEntity<RegistroDiarioDTO> criar(@RequestBody RegistroDiarioDTO dto) {
        var usuario = getUsuarioAutenticado();
        var registro = registroService.salvarRegistro(usuario, dto);

        var resposta = new RegistroDiarioDTO(
                registro.getId(),
                registro.getDataRef(),
                registro.getPontuacaoEquilibrio(),
                registro.getObservacoes(),
                dto.atividades()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public Page<RegistroDiarioDTO> listar(Pageable pageable) {
        var usuario = getUsuarioAutenticado();
        return registroService.listarPorUsuario(usuario, pageable);
    }
}
