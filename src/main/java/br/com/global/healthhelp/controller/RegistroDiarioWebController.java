package br.com.global.healthhelp.controller;

import br.com.global.healthhelp.dto.AtividadeDTO;
import br.com.global.healthhelp.dto.RegistroDiarioDTO;
import br.com.global.healthhelp.model.Usuario;
import br.com.global.healthhelp.repository.CategoriaAtividadeRepository;
import br.com.global.healthhelp.repository.UsuarioRepository;
import br.com.global.healthhelp.service.RegistroDiarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/web/registros")
public class RegistroDiarioWebController {

    private final RegistroDiarioService registroService;
    private final CategoriaAtividadeRepository categoriaRepo;
    private final UsuarioRepository usuarioRepository;

    public RegistroDiarioWebController(RegistroDiarioService registroService,
                                       CategoriaAtividadeRepository categoriaRepo,
                                       UsuarioRepository usuarioRepository) {
        this.registroService = registroService;
        this.categoriaRepo = categoriaRepo;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
    }

    @GetMapping
    public String listarRegistros(
            @PageableDefault(size = 5, sort = "dataRef", direction = Sort.Direction.DESC)
            Pageable pageable,
            Model model
    ) {
        Usuario usuario = getUsuarioAutenticado();

        Page<RegistroDiarioDTO> pagina =
                registroService.listarPorUsuario(usuario, pageable);

        model.addAttribute("pagina", pagina);
        model.addAttribute("activePage", "registros");

        return "registros/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("dataHoje", LocalDate.now());
        model.addAttribute("categorias", categoriaRepo.findAll());
        model.addAttribute("activePage", "registros");
        return "registros/form";
    }

    @PostMapping
    public String salvar(@RequestParam("dataRegistro") LocalDate dataRegistro,
                         @RequestParam(value = "pontuacaoEquilibrio", required = false) Double pontuacaoEquilibrio,
                         @RequestParam(value = "observacoes", required = false) String observacoes,
                         @RequestParam("idCategoria") Long idCategoria,
                         @RequestParam("inicio") String inicio,
                         @RequestParam("fim") String fim) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime inicioDt = LocalDateTime.parse(inicio, formatter);
        LocalDateTime fimDt = LocalDateTime.parse(fim, formatter);

        AtividadeDTO atividade = new AtividadeDTO(
                null,
                idCategoria,
                inicioDt,
                fimDt,
                null,
                null,
                observacoes
        );

        RegistroDiarioDTO dto = new RegistroDiarioDTO(
                null,
                dataRegistro,
                pontuacaoEquilibrio,
                observacoes,
                List.of(atividade)
        );

        Usuario usuario = getUsuarioAutenticado();
        registroService.salvarRegistro(usuario, dto);

        return "redirect:/web/registros";
    }
}
