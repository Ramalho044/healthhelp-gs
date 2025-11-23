package br.com.global.healthhelp.controller;

import br.com.global.healthhelp.model.Usuario;
import br.com.global.healthhelp.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/cadastro";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Usuario usuario,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {

        // Verifica erros de validação
        if (result.hasErrors()) {
            return "usuario/cadastro";
        }

        // Verifica se o email já existe
        if (usuarioService.emailJaExiste(usuario.getEmail())) {
            result.rejectValue("email", "error.usuario", "Este e-mail já está cadastrado.");
            return "usuario/cadastro";
        }

        try {
            usuarioService.cadastrarUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso!");
            return "redirect:/login?cadastrado";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar usuário. Tente novamente.");
            return "redirect:/usuarios/novo";
        }
    }
}
