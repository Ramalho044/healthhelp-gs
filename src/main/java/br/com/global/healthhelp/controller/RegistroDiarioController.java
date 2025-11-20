package br.com.global.healthhelp.controller;

import br.com.global.healthhelp.dto.RecomendacaoDTO;
import br.com.global.healthhelp.dto.RegistroDiarioDTO;
import br.com.global.healthhelp.model.Usuario;
import br.com.global.healthhelp.service.RecomendacaoService;
import br.com.global.healthhelp.service.RegistroDiarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros")
public class RegistroDiarioController {

    private final RegistroDiarioService registroService;
    private final RecomendacaoService recomendacaoService;

    public RegistroDiarioController(RegistroDiarioService registroService,
                                    RecomendacaoService recomendacaoService) {
        this.registroService = registroService;
        this.recomendacaoService = recomendacaoService;
    }

    private Usuario getUsuarioFake() {
        Usuario u = new Usuario();
        u.setId(1L);
        return u;
    }

    @PostMapping
    public ResponseEntity<RegistroDiarioDTO> criar(@RequestBody @Valid RegistroDiarioDTO dto) {
        var usuario = getUsuarioFake();
        var registro = registroService.salvarRegistro(usuario, dto);

        var resposta = new RegistroDiarioDTO(
                registro.getId(),
                registro.getDataRegistro(),
                registro.getPontuacaoEquilibrio(),
                registro.getObservacoes(),
                dto.atividades()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public Page<RegistroDiarioDTO> listar(Pageable pageable) {
        var usuario = getUsuarioFake();
        return registroService.listarPorUsuario(usuario, pageable)
                .map(r -> new RegistroDiarioDTO(
                        r.getId(),
                        r.getDataRegistro(),
                        r.getPontuacaoEquilibrio(),
                        r.getObservacoes(),
                        List.of()
                ));
    }

    @PostMapping("/recomendacoes")
    public ResponseEntity<RecomendacaoDTO> gerarRecomendacao() {
        var usuario = getUsuarioFake();
        var dto = recomendacaoService.gerarRecomendacao(usuario);
        return ResponseEntity.ok(dto);
    }
}
