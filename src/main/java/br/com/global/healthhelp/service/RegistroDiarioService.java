package br.com.global.healthhelp.service;

import br.com.global.healthhelp.dto.AtividadeDTO;
import br.com.global.healthhelp.dto.RegistroDiarioDTO;
import br.com.global.healthhelp.model.Atividade;
import br.com.global.healthhelp.model.RegistroDiario;
import br.com.global.healthhelp.model.Usuario;
import br.com.global.healthhelp.repository.AtividadeRepository;
import br.com.global.healthhelp.repository.CategoriaAtividadeRepository;
import br.com.global.healthhelp.repository.RegistroDiarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RegistroDiarioService {

    private final RegistroDiarioRepository registroRepo;
    private final AtividadeRepository atividadeRepo;
    private final CategoriaAtividadeRepository categoriaRepo;

    public RegistroDiarioService(RegistroDiarioRepository registroRepo,
                                 AtividadeRepository atividadeRepo,
                                 CategoriaAtividadeRepository categoriaRepo) {
        this.registroRepo = registroRepo;
        this.atividadeRepo = atividadeRepo;
        this.categoriaRepo = categoriaRepo;
    }

    @Transactional
    public RegistroDiario salvarRegistro(Usuario usuario, RegistroDiarioDTO dto) {

        var registro = new RegistroDiario();
        registro.setUsuario(usuario);
        registro.setDataRef(dto.dataRegistro());
        registro.setPontuacaoEquilibrio(dto.pontuacaoEquilibrio());
        registro.setObservacoes(dto.observacoes());

        registro = registroRepo.save(registro);

        if (dto.atividades() != null) {
            for (AtividadeDTO a : dto.atividades()) {

                var categoria = categoriaRepo.findById(a.idCategoria())
                        .orElseThrow(() ->
                                new IllegalArgumentException("Categoria inválida: " + a.idCategoria()));

                var atividade = new Atividade();
                atividade.setRegistro(registro);
                atividade.setCategoria(categoria);
                atividade.setInicio(a.inicio());
                atividade.setFim(a.fim());
                atividade.setDescricao(a.descricao());
                atividade.setIntensidade1a5(a.intensidade1a5());
                atividade.setQualidade1a5(a.qualidade1a5());

                atividadeRepo.save(atividade);
            }
        }

        return registro;
    }

    public Page<RegistroDiarioDTO> listarPorUsuario(Usuario usuario, Pageable pageable) {
        return registroRepo.findByUsuario(usuario, pageable)
                .map(registro -> new RegistroDiarioDTO(
                        registro.getId(),
                        registro.getDataRef(),
                        registro.getPontuacaoEquilibrio(),
                        registro.getObservacoes(),
                        null
                ));
    }
}
