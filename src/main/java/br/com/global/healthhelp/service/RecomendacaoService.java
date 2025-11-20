package br.com.global.healthhelp.service;

import br.com.global.healthhelp.dto.RecomendacaoDTO;
import br.com.global.healthhelp.model.Atividade;
import br.com.global.healthhelp.model.Recomendacao;
import br.com.global.healthhelp.model.RegistroDiario;
import br.com.global.healthhelp.model.Usuario;
import br.com.global.healthhelp.repository.AtividadeRepository;
import br.com.global.healthhelp.repository.RecomendacaoRepository;
import br.com.global.healthhelp.repository.RegistroDiarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RecomendacaoService {

    private final RegistroDiarioRepository registroRepo;
    private final AtividadeRepository atividadeRepo;
    private final RecomendacaoRepository recomendacaoRepo;
    private final ChatClient chatClient;

    public RecomendacaoService(RegistroDiarioRepository registroRepo,
                               AtividadeRepository atividadeRepo,
                               RecomendacaoRepository recomendacaoRepo,
                               ChatClient chatClient) {
        this.registroRepo = registroRepo;
        this.atividadeRepo = atividadeRepo;
        this.recomendacaoRepo = recomendacaoRepo;
        this.chatClient = chatClient;
    }

    @Transactional
    public RecomendacaoDTO gerarRecomendacao(Usuario usuario) {
        var registros = registroRepo.findByUsuario(usuario, Pageable.unpaged()).getContent();

        var sb = new StringBuilder();
        sb.append("Analise a rotina do usuário e gere recomendações de bem-estar.");
        sb.append("Descreva em tópicos, em português, destacando pontos de melhoria.\n\n");

        for (RegistroDiario r : registros) {
            sb.append("Dia ").append(r.getDataRegistro())
                    .append(" (score: ").append(r.getPontuacaoEquilibrio()).append(") - ")
                    .append(r.getObservacoes()).append("\n");

            var atividades = atividadeRepo.findByRegistroDiario(r);
            for (Atividade a : atividades) {
                sb.append("  - ").append(a.getCategoria().getNome())
                        .append(" de ").append(a.getInicio())
                        .append(" até ").append(a.getFim())
                        .append(" (").append(a.getDescricao()).append(")\n");
            }
        }

        var response = chatClient.call(new Prompt(sb.toString()));
        String texto = response.getResult().getOutput().getContent();

        var rec = new Recomendacao();
        rec.setUsuario(usuario);
        rec.setMensagem(texto);
        rec.setPontuacaoRelevancia(90);

        rec = recomendacaoRepo.save(rec);

        return new RecomendacaoDTO(rec.getId(), rec.getMensagem(),
                rec.getPontuacaoRelevancia(), rec.getCriadaEm());
    }
}
