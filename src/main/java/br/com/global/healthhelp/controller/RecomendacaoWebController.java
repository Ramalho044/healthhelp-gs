package br.com.global.healthhelp.controller;

import br.com.global.healthhelp.dto.RecomendacaoDTO;
import br.com.global.healthhelp.model.Usuario;
import br.com.global.healthhelp.repository.UsuarioRepository;
import br.com.global.healthhelp.service.RecomendacaoService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/recomendacoes")
public class RecomendacaoWebController {

    private final RecomendacaoService recomendacaoService;
    private final UsuarioRepository usuarioRepository;

    public RecomendacaoWebController(RecomendacaoService recomendacaoService,
                                     UsuarioRepository usuarioRepository) {
        this.recomendacaoService = recomendacaoService;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
    }

    @GetMapping
    public String gerar(Model model) {
        Usuario usuario = getUsuarioAutenticado();
        RecomendacaoDTO dto = recomendacaoService.gerarRecomendacao(usuario);
        model.addAttribute("recomendacao", dto);
        String htmlContent = convertMarkdownToHtml(dto.texto());
        model.addAttribute("renderedHtml", htmlContent);
        return "recomendacoes/detalhes";
    }

    public String convertMarkdownToHtml(String markdownContent) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownContent);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
